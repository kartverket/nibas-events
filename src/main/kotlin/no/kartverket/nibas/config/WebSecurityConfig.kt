package no.kartverket.nibas.config

import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.authentication.AuthenticationFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import java.util.logging.Logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
@Profile("!security-off")
class WebSecurityConfig(private val environment: Environment) {

    companion object {
        const val ORDER_OF_PUBLISHER_API_KEY_FILTER_CHAIN: Int =
            SecurityProperties.BASIC_AUTH_ORDER - 2 // høyeste pri
        const val ORDER_OF_CONSUMER_API_KEY_FILTER_CHAIN: Int =
            SecurityProperties.BASIC_AUTH_ORDER - 1 // laveste pri

        const val SECURITY_OFF_PROFILE_STRING = "security-off"
        const val PRODUCTION_PROFILE_STRING = "prod"
    }

    @Order(ORDER_OF_PUBLISHER_API_KEY_FILTER_CHAIN)
    @Bean
    fun publisherApiKeySecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        val kac =
            KeyAuthenticationConverter(
                mapOf(
                    "Publisher" to
                        {
                            environment.getRequiredProperty("api.key.publisher")
                        }
                )
            )

        val apiKeyFilter = AuthenticationFilter(KeyAuthenticationManager(), kac)
        // Do nothing on successHandler, return response from original url
        apiKeyFilter.successHandler = AuthenticationSuccessHandler { _, _, _ -> }

        return commonFilterChainConfig(
            kac,
            httpSecurity.securityMatchers { it.requestMatchers(HttpMethod.POST, "/v1/events") }
        )
    }

    @Order(ORDER_OF_CONSUMER_API_KEY_FILTER_CHAIN)
    @Bean
    fun consumerApiKeySecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        val kac =
            KeyAuthenticationConverter(
                mapOf("Consumer" to { environment.getRequiredProperty("api.key.consumer") })
            )

        val apiKeyFilter = AuthenticationFilter(KeyAuthenticationManager(), kac)
        // Do nothing on successHandler, return response from original url
        apiKeyFilter.successHandler = AuthenticationSuccessHandler { _, _, _ -> }

        return commonFilterChainConfig(kac, httpSecurity.securityMatcher("/v1/events"))
    }

    private fun commonFilterChainConfig(
        kac: KeyAuthenticationConverter,
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {

        val apiKeyFilter = AuthenticationFilter(KeyAuthenticationManager(), kac)
        // Do nothing on successHandler, return response from original url
        apiKeyFilter.successHandler = AuthenticationSuccessHandler { _, _, _ -> }

        return httpSecurity
            .addFilterAfter(apiKeyFilter, AbstractPreAuthenticatedProcessingFilter::class.java)
            .exceptionHandling { it.authenticationEntryPoint(UnauthorizedEntryPoint()) }
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .logout()
            .disable()
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .build()
    }
}

@Configuration
@EnableWebSecurity
@Profile("security-off")
class NoSecurityConfig(private val environment: Environment) {
    private val logger = Logger.getLogger(this::class.java.name)

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        if (environment.activeProfiles.contains(WebSecurityConfig.PRODUCTION_PROFILE_STRING)) {
            throw RuntimeException(
                "Tjener er feilkonfigurert. Kan ikke starte med profil ${WebSecurityConfig.SECURITY_OFF_PROFILE_STRING} i prod."
            )
        }
        logger.warning(
            "\n\n\n---------\nOBS!! Started app with SECURITY SWITCHED OFF!\n---------\n\n"
        )
        return http.csrf().disable().authorizeHttpRequests { it.anyRequest().permitAll() }.build()
    }
}

class UnauthorizedEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
    }
}

class KeyAuthenticationManager : AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication.credentials != null) {
            authentication.isAuthenticated = true
        }
        return authentication
    }
}

class KeyAuthenticationConverter(apiKeySuppliers: Map<String, () -> String>) :
    AuthenticationConverter {
    private val apiKeys = apiKeySuppliers.entries.map { (k, v) -> KeyAuthenticationToken(v, k) }

    override fun convert(request: HttpServletRequest): Authentication? {
        return request.getHeader("Authorization")?.let { lookup(it.replace("Basic ", "")) }
    }

    private fun lookup(apiKey: String) = apiKeys.find { it.credentials == apiKey }
}

class KeyAuthenticationToken(private val keySupplier: () -> String, private val principal: String) :
    Authentication {
    private var authenticated = false

    override fun getName(): String = principal
    override fun getAuthorities() = null
    override fun getCredentials() = keySupplier()
    override fun getDetails() = null
    override fun getPrincipal() = principal
    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }
}
