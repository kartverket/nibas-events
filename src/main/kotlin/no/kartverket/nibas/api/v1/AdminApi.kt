package no.kartverket.nibas.api.v1

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping


@RequestMapping("/v1/admin")
@Tag(name = "v1/admin", description = "Endepunkter for å gjøre administrative endringer i Nibas events")
interface AdminApi {

    @Operation(summary = "Stopper å lytte på events fra nibas-backend")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful operation",
        )
    )
    @PutMapping("/pubsub/stop")
    suspend fun stopPubSubListener()

    @Operation(summary = "Starter å lytte på events fra nibas-backend")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successful operation",
        )
    )
    @PutMapping("/pubsub/start")
    suspend fun startPubSubListener()

}
