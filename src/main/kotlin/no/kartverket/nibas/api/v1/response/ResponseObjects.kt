package no.kartverket.nibas.api.v1.response

import io.swagger.v3.oas.annotations.media.Schema
import no.kartverket.nibas.api.v1.common.ObjektType
import no.kartverket.nibas.api.v1.common.EventType
import java.time.LocalDate
import java.time.ZonedDateTime

@Schema(description = "En representasjon av en event")
data class EventResponse(

    @Schema(description = "Unik identifikator til event")
    val uuid: String?,

    @Schema(description = "Eventnummer (løpenummer)")
    val eventnummer: Long,

    @Schema(description = "Når eventen inntreffer")
    val inntreffer: LocalDate,

    @Schema(description = "Når eventen skjedde")
    val timestamp: ZonedDateTime,

    @Schema(description = "Radene eventet består av")
    val rader: Set<EventResponseRad>
)

data class EventResponseRad(
    @Schema(description = "Eventradens unike id")
    val uuid: String,

    @Schema(description = "Hvilket event har inntruffet")
    val type: EventType,

    @Schema(description = "Hvilket type objekt har event skjedd for")
    val objektType: ObjektType,

    @Schema(description = "Lokalid til objektet eventen peker på")
    val lokalId: String,

    @Schema(description = "Hvilket event hører raden til")
    val event: String?,
)
