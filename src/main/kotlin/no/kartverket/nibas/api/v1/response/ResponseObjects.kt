package no.kartverket.nibas.api.v1.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "En representasjon av en event")
data class EventResponse(

    @Schema(description = "ID-en til eventen")
    val id: Long,

    @Schema(description = "Hvilken event har inntruffet")
    val type: EventType,

    @Schema(description = "Hvilket type objekt har event skjedd for")
    val target: EventTarget,

    @Schema(description = "URL til representasjon av objektet etter event har inntruffet")
    val href: String
)

enum class EventType {
    OPPDATERT
}

enum class EventTarget {
    STEMMEKRETS, GRUNNKRETS
}
