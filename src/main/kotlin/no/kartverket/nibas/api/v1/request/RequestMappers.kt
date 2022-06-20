package no.kartverket.nibas.api.v1.request

import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventTarget
import no.kartverket.nibas.domain.EventType
import java.util.UUID

fun EventRequest.toEvent(offset: Long): Event = Event(
    id = 0,
    uuid = this.uuid,
    offset = offset,
    type = EventType.valueOf(this.type.name),
    target = EventTarget.valueOf(this.target.name),
    targetId = this.id,
    targetRevision = this.revision,
    timestamp = this.timestamp)
