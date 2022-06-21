package no.kartverket.nibas.controller

import kotlinx.coroutines.runBlocking
import no.kartverket.nibas.api.v1.AdminApi
import no.kartverket.nibas.config.NibasEventsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
class AdminController constructor(val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry) : AdminApi {

    val logger = Logger.getLogger(this::class.java.name)

    // TODO: NIBAS-486: Disse burde ha autentisering på seg
    override suspend fun stopKafkaListener() {
        logger.warning("Stopping kafka listener")
        runBlocking {
            kafkaListenerEndpointRegistry.getListenerContainer(NibasEventsConfig.nibasEventsListenerId)?.stop()
        }
    }

    // TODO: NIBAS-486: Disse burde ha autentisering på seg
    override suspend fun startKafkaListener() {
        logger.warning("Starting kafka listener")
        runBlocking {
            kafkaListenerEndpointRegistry.getListenerContainer(NibasEventsConfig.nibasEventsListenerId)?.start()
        }
    }
}
