package no.kartverket.nibas.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.kartverket.nibas.api.v1.request.EventRequest
import no.kartverket.nibas.api.v1.response.EventResponse
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus

@RequestMapping("/v1/events")
@Tag(name = "v1/events", description = "Endepunkter for events i Nasjonal inndelingsbase")
interface EventsApi {
    @Operation(summary = "Hent events", description = "Henter events i Nasjonal Inndelingsbase", operationId = "hentEvents")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Successful operation",))
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun hentEvents(
        @Parameter(description = "page") @RequestParam(name = "page", required = false) page: Int?,
        @Parameter(description = "size") @RequestParam(name = "size", required = false) size: Int?
    ): Page<EventResponse>

    @Operation(description = "Publiserer event til Nibas Event")
    @ApiResponses(ApiResponse(responseCode = "201",description = "Created",))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping( "/publiser", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun publiserEvent(
        @Parameter(description = "event") @RequestBody(required = true) eventRequest: EventRequest
    )
}
