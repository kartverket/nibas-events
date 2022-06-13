package no.kartverket.nibas.api.v1.request

import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventTarget
import no.kartverket.nibas.domain.EventType

fun EventRequest.toEvent(): Event = Event(0, EventType.valueOf(this.type.name), EventTarget.valueOf(this.target.name), this.href)
