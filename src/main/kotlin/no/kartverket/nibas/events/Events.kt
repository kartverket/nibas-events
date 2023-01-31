package no.kartverket.nibas.events

import no.kartverket.nibas.api.v1.EventsApi
import no.kartverket.nibas.api.v1.request.EventRequest
import no.kartverket.nibas.api.v1.request.toEvent
import no.kartverket.nibas.api.v1.response.EventResponse
import no.kartverket.nibas.api.v1.response.toEventResponse
import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.repository.EventRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

@RestController
class EventsController(
    private val eventService: EventService
) : EventsApi {
    val logger: Logger = Logger.getLogger(this::class.java.name)

    override fun hentEvents(page: Int?, size: Int?): Page<EventResponse> {
        val pageRequest = PageRequest.of(page ?: 0, size ?: 100)
        val pageElements = eventService.findEventsBy(pageRequest).map { it.toEventResponse() }
        return PageImpl(pageElements.toList(), pageRequest, eventService.getTotalAntallEvents())
    }

    override fun publiserEvent(eventRequest: EventRequest) {
        val event = eventService.lagreEvent(eventRequest.toEvent())
        logger.info("Event lagret: ${event.id}. Rader: ${event.eventRader.map { it.uuid }}")
    }
}

@Service
class EventService(
    private val eventRepository: EventRepository
) {
    @Transactional
    fun lagreEvent(event: Event) : Event {
        return eventRepository.save(event)
    }
    @Transactional(readOnly = true)
    fun findEventsBy(pageRequest: PageRequest): List<Event> {
        return eventRepository.findAllBy(pageRequest)
    }
    @Transactional(readOnly = true)
    fun getTotalAntallEvents(): Long {
        return eventRepository.count()
    }
}
