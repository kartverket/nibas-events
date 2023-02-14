package no.kartverket.nibas.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.OffsetDateTime

@Table(name = "event")
data class Event(
    @Id val id: Int,
    @Column("inntreffer") val inntreffer: LocalDate,
    @Column("event_timestamp") val timestamp: OffsetDateTime,
    @MappedCollection(idColumn = "event_fk") val eventRader: Set<EventRad>
)

@Table(name = "eventrad")
data class EventRad(
    @Id val uuid: String,
    @Column("event_type") val eventType: EventType,
    @Column("flate_type") val flateType: FlateType,
    @Column("lokalid") val lokalId: String,
    @Column("event_fk") val eventId: Int
)

enum class EventType {
    ADDED, MODIFIED, EXPIRED
}

enum class FlateType {
    STEMMEKRETS, GRUNNKRETS, KOMMUNE, FYLKE, NASJON
}
