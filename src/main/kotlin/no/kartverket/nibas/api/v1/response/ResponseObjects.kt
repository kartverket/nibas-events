package no.kartverket.nibas.api.v1.response

import io.swagger.v3.oas.annotations.media.Schema
import no.kartverket.nibas.api.v1.common.EventTarget
import no.kartverket.nibas.api.v1.common.EventType
import java.time.LocalDate
import java.time.ZonedDateTime

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

    @Schema(description = "Lokalid til objektet eventen peker på")
    val targetId: String,

    @Schema(description = "Når eventen skjedde")
    val timestamp: ZonedDateTime,

    @Schema(description = "Når eventen er gyldig fra")
    val gyldigfra: LocalDate,

    @Schema(description = "Når eventen er gyldig til, kan være null")
    val gyldigtil: LocalDate?
)
