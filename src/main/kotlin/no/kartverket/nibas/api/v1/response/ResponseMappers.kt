package no.kartverket.nibas.api.v1.response

import no.kartverket.nibas.api.v1.common.EventTarget
import no.kartverket.nibas.api.v1.common.EventType
import no.kartverket.nibas.domain.Event

fun Event.toEventResponse(): EventResponse {
    return EventResponse(this.id, EventType.valueOf(this.type.name), EventTarget.valueOf(this.target.name), this.targetId)
}
