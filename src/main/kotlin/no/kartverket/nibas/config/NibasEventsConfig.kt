package no.kartverket.nibas.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler
import org.springframework.kafka.listener.CommonErrorHandler


@Configuration
class NibasEventsConfig {

    companion object {
        const val nibasEventsListenerId = "nibas-events-listener"
        const val nibasEventsTopic = "nibas-events"
    }


    @Bean
    fun openApiDef(): OpenAPI {
        return OpenAPI().info(Info().title("NIBAS Events API").description("API for å hente events i Nasjonal inndelingsbase").version("v1"))
    }

    @Bean
    fun errorHandler(): CommonErrorHandler {
        return CommonContainerStoppingErrorHandler()
    }
}
