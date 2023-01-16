package no.kartverket.nibas.api.v1.common

enum class EventType {
    ADDED, MODIFIED, DELETED, EXPIRED
}

enum class EventTarget {
    STEMMEKRETS, GRUNNKRETS, SKOLEKRETS, KOMMUNE, FYLKE, NASJON
}
