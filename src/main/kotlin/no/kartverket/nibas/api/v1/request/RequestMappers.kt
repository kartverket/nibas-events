package no.kartverket.nibas.api.v1.request

import com.google.protobuf.Timestamp
import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventTarget
import no.kartverket.nibas.domain.EventType
import java.time.Instant
import java.time.ZonedDateTime
import java.util.TimeZone

fun EventRequest.toEvent(timestamp: Timestamp): Event = Event(
    id = 0,
    uuid = this.uuid,
    type = EventType.valueOf(this.type.name),
    target = EventTarget.valueOf(this.target.name),
    targetId = this.id,
    targetRevision = this.revision,
    timestamp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong()), TimeZone.getDefault().toZoneId()))

