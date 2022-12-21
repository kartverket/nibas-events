package no.kartverket.nibas.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.LocalDate

@Table(name = "events")
data class Event(
    @Id val id: Long,
    @Column("event_uuid") val uuid: String,
    @Column("event_type") val type: EventType,
    @Column("target") val target: EventTarget,
    @Column("target_id") val targetId: String,
    @Column("event_timestamp") val timestamp: LocalDateTime,
    @Column("gyldigfra") val gyldigfra: LocalDate,
    @Column("gyldigtil") val gyldigtil: LocalDate?
)

enum class EventType {
    ADDED, MODIFIED, DELETED
}

enum class EventTarget {
    STEMMEKRETS, GRUNNKRETS, SKOLEKRETS, KOMMUNE, FYLKE, NASJON
}
