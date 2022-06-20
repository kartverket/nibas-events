package no.kartverket.nibas.api.v1.response

import io.swagger.v3.oas.annotations.media.Schema
import no.kartverket.nibas.api.v1.common.EventTarget
import no.kartverket.nibas.api.v1.common.EventType
import java.sql.Timestamp
import java.time.Instant

@Schema(description = "En representasjon av en event")
data class EventResponse(

    @Schema(description = "Unik identifikator til event")
    val uuid: String,

    @Schema(description = "Rekkefølge på eventen")
    val offset: Long,

    @Schema(description = "Hvilken event har inntruffet")
    val type: EventType,

    @Schema(description = "Hvilket type objekt har event skjedd for")
    val target: EventTarget,

    @Schema(description = "Identifikatoren til objektet eventen peker på")
    val targetId: String,

    @Schema(description = "Id til gjeldende revisjon av objektet")
    val targetRevision: Int,

    @Schema(description = "Når eventen skjedde")
    val timestamp: Instant
)
