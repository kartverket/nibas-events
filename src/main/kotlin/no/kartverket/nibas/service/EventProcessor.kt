package no.kartverket.nibas.service

import kotlinx.coroutines.runBlocking
import no.kartverket.nibas.api.v1.request.EventRequest
import no.kartverket.nibas.api.v1.request.toEvent
import no.kartverket.nibas.config.NibasEventsConfig
import no.kartverket.nibas.repository.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*
import java.util.logging.Logger

@Service
class EventProcessor @Autowired constructor(val eventRepository: EventRepository) {

    val logger = Logger.getLogger(this::class.java.name)


    @KafkaListener(id = NibasEventsConfig.nibasEventsListenerId, topics = [NibasEventsConfig.nibasEventsTopic])
    fun consume(eventRequest: EventRequest,
                @Header(KafkaHeaders.OFFSET) offset: Long,
                @Header(KafkaHeaders.RECEIVED_TIMESTAMP) timestamp: Long) {

        val event = eventRequest.toEvent(offset, timestamp)
        logger.info("Consumed event: ${event.type}: ${event.target} med id ${event.id} (${event.timestamp}) -  OFFSET = ${event.offset}")

        runBlocking {
            eventRepository.save(event)
        }

    }
}
