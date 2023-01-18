package no.kartverket.nibas.api.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import no.kartverket.nibas.api.v1.common.EventTarget
import no.kartverket.nibas.api.v1.common.EventType
import java.time.LocalDate

@Schema(description = "En representasjon av en event")
data class EventRequest(

    @Schema(description = "UUID for denne eventen")
    val uuid: String,

    @Schema(description = "Hvilken event har inntruffet")
    val type: EventType,

    @Schema(description = "Hvilket type objekt har event skjedd for")
    val target: EventTarget,

    @Schema(description = "Lokalid for objektet")
    val lokalid: String,

    @Schema(description = "Når eventen er gyldig fra")
    val gyldigFra: LocalDate,

    @Schema(description = "Når eventen er gyldig til, kan være null")
    val gyldigTil: LocalDate?
)
