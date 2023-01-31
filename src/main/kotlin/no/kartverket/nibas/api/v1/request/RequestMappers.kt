package no.kartverket.nibas.api.v1.request

import com.google.protobuf.Timestamp
import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventRad
import no.kartverket.nibas.domain.ObjektType
import no.kartverket.nibas.domain.EventType
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone
import java.util.UUID

fun EventRequest.toEvent(timestamp: Timestamp): Event {
    val eventUuid = UUID.randomUUID().toString()

    return Event(
        eventNummer = 0,
        uuid = eventUuid,
        inntreffer = inntreffer,
        timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong()), TimeZone.getDefault().toZoneId()),
        eventRader = this.eventRader.map {
            EventRad(
                uuid = UUID.randomUUID().toString(),
                eventType = EventType.valueOf(it.eventType.name),
                objektType = ObjektType.valueOf(it.objektType.name),
                lokalId = it.lokalid,
                event = eventUuid
            )
        }.toSet()
    )
}
