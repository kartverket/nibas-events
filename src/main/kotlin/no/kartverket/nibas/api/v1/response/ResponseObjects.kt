package no.kartverket.nibas.api.v1.response

import io.swagger.v3.oas.annotations.media.Schema
import no.kartverket.nibas.api.v1.common.FlateType
import no.kartverket.nibas.api.v1.common.EventType
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(description = "En representasjon av en event")
data class EventResponse(

    @Schema(description = "Eventnummer (løpenummer)")
    val id: Long,

    @Schema(description = "Når eventen inntreffer")
    val inntreffer: LocalDate,

    @Schema(description = "Når eventen skjedde")
    val timestamp: LocalDateTime,

    @Schema(description = "Radene eventet består av")
    val rader: Set<EventResponseRad>
)

@Schema(description = "Rad med data om endringen")
data class EventResponseRad(
    @Schema(description = "Hvilket event har inntruffet")
    val type: EventType,

    @Schema(description = "Hvilket type objekt har event skjedd for")
    val flateType: FlateType,

    @Schema(description = "Lokalid til objektet. Brukes mot Nibas-API")
    val lokalId: String,

    @Schema(description = "Hvilket event hører raden til")
    val eventId: Int,
)
