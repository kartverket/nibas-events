package no.kartverket.nibas.api.v1.request

import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventRad
import no.kartverket.nibas.domain.EventType
import no.kartverket.nibas.domain.FlateType
import java.time.OffsetDateTime

fun EventRequest.toEvent(): Event {
    return Event(
        id = 0, // en @Id som er null eller 0 vil tolkes som new fra Spring Data JDBC, og dermed telles opp
        inntreffer = inntreffer,
        timestamp = OffsetDateTime.now(),
        eventRader = this.eventRader.map {
            EventRad(
                uuid = it.uuid,
                eventType = EventType.valueOf(it.eventType.name),
                flateType = FlateType.valueOf(it.flateType.name),
                lokalId = it.lokalid,
                eventId = 0
            )
        }.toSet()
    )
}
