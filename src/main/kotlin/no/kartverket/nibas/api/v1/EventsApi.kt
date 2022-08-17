package no.kartverket.nibas.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.kartverket.nibas.api.v1.response.EventResponse
import org.springframework.data.domain.Page
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/v1/events")
@Tag(name = "v1/events", description = "Endepunkter for events i Nasjonal inndelingsbase")
interface EventsApi {
    @Operation(summary = "Hent events", description = "Henter events i Nasjonal Inndelingsbase", operationId = "hentEvents")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful operation",
        )
    )
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun hentEvents(
        @Parameter(description = "page")
        @RequestParam(name = "page", required = false) page: Int?,
        @Parameter(description = "size")
        @RequestParam(name = "size", required = false) size: Int?): Page<EventResponse>
}
