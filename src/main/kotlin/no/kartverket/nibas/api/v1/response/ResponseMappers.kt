package no.kartverket.nibas.api.v1.response

import no.kartverket.nibas.api.v1.common.ObjektType
import no.kartverket.nibas.api.v1.common.EventType
import no.kartverket.nibas.domain.Event
import java.util.*

fun Event.toEventResponse(): EventResponse {
    return EventResponse(
        uuid = this.uuid,
        eventnummer = this.eventNummer,
        inntreffer = this.inntreffer,
        timestamp = this.timestamp.atZone(TimeZone.getDefault().toZoneId()),
        rader = this.eventRader.map {
            EventResponseRad(
                uuid = it.uuid,
                type = EventType.valueOf(it.eventType.name),
                objektType = ObjektType.valueOf(it.objektType.name),
                lokalId = it.lokalId,
                event = it.event
            )
        }.toSet(),
    )
}
