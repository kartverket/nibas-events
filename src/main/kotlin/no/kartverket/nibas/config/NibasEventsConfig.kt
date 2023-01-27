package no.kartverket.nibas.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class NibasEventsConfig {

    @Bean
    fun openApiDef(): OpenAPI {
        return OpenAPI().info(Info().title("NIBAS Events API").description("API for å hente events i Nasjonal inndelingsbase").version("v1"))
    }
}
