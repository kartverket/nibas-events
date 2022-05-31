package no.kartverket.nibas.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.kartverket.nibas.api.v1.response.EventResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/v1/events")
@Tag(name = "v1/events", description = "Endepunkter for events i Nasjonal inndelingsbase")
interface EventsApi {
    @Operation(summary = "Hent events", description = "Henter events i Nasjonal Inndelingsbase", operationId = "hentEvents")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = [Content(
                mediaType = "application/json",
                array = ArraySchema(schema = Schema(implementation = EventResponse::class))
            )]
        )
    )
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hentEvents(): List<EventResponse>
}
