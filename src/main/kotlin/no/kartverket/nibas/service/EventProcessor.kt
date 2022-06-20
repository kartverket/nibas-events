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
import java.util.logging.Logger

@Service
class EventProcessor @Autowired constructor(val eventRepository: EventRepository) {

    val logger = Logger.getLogger(this::class.java.name)


    @KafkaListener(id = NibasEventsConfig.nibasEventsListenerId, topics = [NibasEventsConfig.nibasEventsTopic])
    fun consume(event: EventRequest, @Header(KafkaHeaders.OFFSET) offset: Long) {
        logger.info("Consumed eventrequest: ${event.type}: ${event.target} med id ${event.id} (${event.timestamp.toString()}) -  OFFSET = $offset")
        runBlocking {
            eventRepository.save(event.toEvent(offset))
        }

    }
}
