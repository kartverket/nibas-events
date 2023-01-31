package no.kartverket.nibas.domain

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.time.LocalDate

@Table(name = "event")
data class Event(
    @Id val uuid: String,
    @Column("eventnummer") val eventNummer: Long,
    @Column("inntreffer") val inntreffer: LocalDate,
    @Column("event_timestamp") val timestamp: LocalDateTime,
    @MappedCollection val eventRader: Set<EventRad>
) : Persistable<String> {
    override fun getId(): String { return uuid }
    // i nibas-events vil alle events være nye
    override fun isNew(): Boolean { return true }
}

@Table(name = "eventrad")
data class EventRad(
    @Id val uuid: String,
    @Column("event_type") val eventType: EventType,
    @Column("objekt_type") val objektType: ObjektType,
    @Column("lokalid") val lokalId: String,
    @Column("event") val event: String
)

enum class EventType {
    ADDED, MODIFIED, EXPIRED
}

enum class ObjektType {
    STEMMEKRETS, GRUNNKRETS, SKOLEKRETS, KOMMUNE, FYLKE, NASJON
}
