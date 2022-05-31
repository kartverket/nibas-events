package no.kartverket.nibas.events

import no.kartverket.nibas.api.v1.EventsApi
import no.kartverket.nibas.api.v1.response.EventResponse
import no.kartverket.nibas.api.v1.response.EventTarget
import no.kartverket.nibas.api.v1.response.EventType
import org.springframework.web.bind.annotation.RestController

@RestController
class EventsController : EventsApi {
    override fun hentEvents() = listOf(
        EventResponse(1, EventType.OPPDATERT, EventTarget.GRUNNKRETS, "http://nibas-api/v1/grunnkretser/1"),
        EventResponse(2, EventType.OPPDATERT, EventTarget.GRUNNKRETS, "http://nibas-api/v1/grunnkretser/2")
    )
}
