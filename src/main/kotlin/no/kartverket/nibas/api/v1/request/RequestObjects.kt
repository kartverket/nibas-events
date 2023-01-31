package no.kartverket.nibas.api.v1.request

import io.swagger.v3.oas.annotations.media.Schema
import no.kartverket.nibas.api.v1.common.FlateType
import no.kartverket.nibas.api.v1.common.EventType
import java.time.LocalDate

@Schema(description = "En representasjon av en event")
data class EventRequest(

    @Schema(description = "Når eventen er gyldig fra")
    val inntreffer: LocalDate,

    @Schema(description = "Liste med relaterte rader")
    val eventRader: Set<EventRadRequest>
)

data class EventRadRequest(

    @Schema(description = "Objektradens id")
    val uuid: String,

    @Schema(description = "Hvilken event har inntruffet")
    val eventType: EventType,

    @Schema(description = "Hvilket type objekt har event skjedd for")
    val flateType: FlateType,

    @Schema(description = "Lokalid for objektet")
    val lokalid: String,
)
