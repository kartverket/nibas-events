package no.kartverket.nibas.api.v1.response

import no.kartverket.nibas.api.v1.common.FlateType
import no.kartverket.nibas.api.v1.common.EventType
import no.kartverket.nibas.domain.Event

fun Event.toEventResponse(): EventResponse {
    return EventResponse(
        id = this.id,
        inntreffer = this.inntreffer,
        timestamp = this.timestamp,
        rader = this.eventRader.map {
            EventResponseRad(
                type = EventType.valueOf(it.eventType.name),
                flateType = FlateType.valueOf(it.flateType.name),
                lokalId = it.lokalId,
                eventId = it.eventId
            )
        }.toSet(),
    )
}
