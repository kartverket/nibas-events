package no.kartverket.nibas.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.logging.Logger

@Configuration
@EnableWebFluxSecurity
class WebSecurityConfig constructor(private val environment: Environment) {

    companion object {
        const val SECURITY_OFF_PROFILE_STRING = "security-off"
        const val PRODUCTION_PROFILE_STRING = "prod"
    }

    private val logger = Logger.getLogger(this::class.java.name)

    @Bean
    fun apiKeySecurityFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        return if (startedWithSecurityOff()) {
           filterChainForSecurityOff(httpSecurity)
        } else {
            filterChainForApiKey(httpSecurity)
        }
    }

    private fun filterChainForApiKey(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        val kac = KeyAuthenticationConverter(mapOf(
            "Matrikkel" to { environment.getRequiredProperty("api.key.matrikkel") }
        ))

        val apiKeyFilter = AuthenticationWebFilter(KeyAuthenticationManager())
        apiKeyFilter.setServerAuthenticationConverter(kac)

        return httpSecurity.authorizeExchange()
            .pathMatchers("/actuator/health",
                "/actuator/info",
                "/api-docs/**",
                "/swagger-ui/**").permitAll()
            .anyExchange().authenticated()
            .and()
            .addFilterAt(apiKeyFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .exceptionHandling().authenticationEntryPoint(UnauthorizedEntryPoint()).and()
            .httpBasic().disable()
            .csrf().disable()
            .logout().disable()
            .formLogin().disable()
            .build()
    }

    private fun filterChainForSecurityOff(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        if (inProduction()) {
            throw RuntimeException("Tjener er feilkonfigurert. Kan ikke starte med profil $SECURITY_OFF_PROFILE_STRING i prod.")
        } else {
            logger.warning("\n\n\n---------\nOBS!! Started app with SECURITY SWITCHED OFF!\n---------\n\n")
            return httpSecurity.csrf().disable().build()
        }
    }

    fun inProduction() = environment.hasActiveProfile(PRODUCTION_PROFILE_STRING)
    fun startedWithSecurityOff() = environment.hasActiveProfile(SECURITY_OFF_PROFILE_STRING)
}

class UnauthorizedEntryPoint : ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        return Mono.empty()
    }
}

class KeyAuthenticationManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        return Mono.fromSupplier {
            if (authentication != null && authentication.credentials != null) {
                authentication.isAuthenticated = true
            }
            authentication
        }
    }

}

class KeyAuthenticationConverter(apiKeySuppliers: Map<String, () -> String>) : ServerAuthenticationConverter {

    private val apiKeys = apiKeySuppliers.entries.map { (k, v) ->
        KeyAuthenticationToken(v, k)
    }

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange)
            .flatMap { serverWebExchange -> Mono.justOrEmpty(serverWebExchange.request.headers["Authorization"]) }
            .filter { headerValues -> headerValues.isNotEmpty() }
            .flatMap { headerValues -> removeBasic(headerValues[0]) }
            .flatMap { apiKey -> lookup(apiKey) }
    }

    private fun removeBasic(apiKey: String) = Mono.justOrEmpty(apiKey)
        .filter { key -> key.startsWith("Basic ") }
        .flatMap { key -> Mono.justOrEmpty(key.replace("Basic ", "")) }

    private fun lookup(apiKey: String) = Mono.justOrEmpty(apiKeys.find {
        it.credentials == apiKey
    })
}

class KeyAuthenticationToken(private val keySupplier: () -> String, private val principal: String) : Authentication {
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


fun Environment.hasActiveProfile(profileString: String): Boolean =
    listOf(*this.activeProfiles).contains(profileString)
