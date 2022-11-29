package no.kartverket.nibas.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders
import kotlinx.coroutines.runBlocking
import no.kartverket.nibas.api.v1.request.EventRequest
import no.kartverket.nibas.api.v1.request.toEvent
import no.kartverket.nibas.repository.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
@ConditionalOnProperty(value = ["spring.cloud.gcp.pubsub.enabled"], havingValue = "true")
class EventProcessor @Autowired constructor(val eventRepository: EventRepository) {

    val logger = Logger.getLogger(this::class.java.name)
    val mapper = ObjectMapper().registerModule(KotlinModule.Builder().build())

    @ServiceActivator(inputChannel = "inputMessageChannel")
    fun messageReceiver(payload: String, @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) message: BasicAcknowledgeablePubsubMessage) {
        val timestamp = message.pubsubMessage.publishTime
        val eventRequest = mapper.readValue(payload, EventRequest::class.java)
        val event = eventRequest.toEvent(timestamp = timestamp)

        runBlocking {
            val toSave = when (val existing = eventRepository.findByUuid(event.uuid)) {
                null -> event
                else -> existing.copy(
                    type = event.type,
                    target = event.target,
                    targetId = event.targetId,
                    targetRevision = event.targetRevision,
                    timestamp = event.timestamp)
            }
            eventRepository.save(toSave)
        }

        message.ack()
        logger.info("Consumed event: ${event.type}: ${event.target} med id ${event.id} (${event.timestamp})")
    }

}
