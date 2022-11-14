package no.kartverket.nibas.api.v1.response

import no.kartverket.nibas.api.v1.common.EventTarget
import no.kartverket.nibas.api.v1.common.EventType
import no.kartverket.nibas.domain.Event

fun Event.toEventResponse(): EventResponse {
    return EventResponse(
        uuid = this.uuid,
        offset = this.id,
        type = EventType.valueOf(this.type.name),
        target = EventTarget.valueOf(this.target.name),
        targetId = this.targetId,
        targetRevision = this.targetRevision,
        timestamp = this.timestamp)
}
