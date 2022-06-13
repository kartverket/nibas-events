package no.kartverket.nibas.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(name = "events")
data class Event(
    @Id val id: Long,
    @Column("event_type") val type: EventType,
    @Column("target") val target: EventTarget,
    @Column("target_id") val targetId: String
)

enum class EventType {
    OPPDATERT
}

enum class EventTarget {
    STEMMEKRETS, GRUNNKRETS
}
