package no.kartverket.nibas.controller

import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter
import kotlinx.coroutines.runBlocking
import no.kartverket.nibas.api.v1.AdminApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
@ConditionalOnProperty(value = ["spring.cloud.gcp.pubsub.enabled"], havingValue = "true")
class AdminController constructor(val inboundChannelAdapter: PubSubInboundChannelAdapter) : AdminApi {

    val logger = Logger.getLogger(this::class.java.name)

    // TODO: NIBAS-486: Disse burde ha autentisering på seg
    override suspend fun stopPubSubListener() {
        logger.warning("Stopping pubsub listener")
        runBlocking {
            inboundChannelAdapter.stop()
        }
    }

    // TODO: NIBAS-486: Disse burde ha autentisering på seg
    override suspend fun startPubSubListener() {
        logger.warning("Starting pubsub listener")
        runBlocking {
            inboundChannelAdapter.start()
        }
    }
}
