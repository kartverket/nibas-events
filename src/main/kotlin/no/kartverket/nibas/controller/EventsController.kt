package no.kartverket.nibas.controller

import no.kartverket.nibas.api.v1.EventsApi
import no.kartverket.nibas.api.v1.response.EventResponse
import no.kartverket.nibas.api.v1.response.toEventResponse
import no.kartverket.nibas.repository.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class EventsController @Autowired constructor(
    private val eventRepository: EventRepository
) : EventsApi {

    override fun hentEvents(page: Int?, size: Int?): Page<EventResponse> {
        val pageRequest = PageRequest.of(page ?: 0, size ?: 100)
        val pageElements = eventRepository.findAllBy(pageRequest).map { it.toEventResponse() }
        return PageImpl(pageElements.toList(), pageRequest, eventRepository.count())
    }
}

