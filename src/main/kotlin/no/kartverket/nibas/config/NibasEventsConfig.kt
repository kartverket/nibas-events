package no.kartverket.nibas.config

import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import no.kartverket.nibas.events.ManglendeEventRadException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@Configuration
class NibasEventsConfig {

    @Bean
    fun openApiDef(): OpenAPI {
        return OpenAPI().info(Info().title("NIBAS Events API").description("API for å hente events i Nasjonal inndelingsbase").version("v1"))
    }
}

@ControllerAdvice
class CustomRestExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ManglendeEventRadException::class])
    fun handleManglendeEventRadException(ex: ManglendeEventRadException, request: WebRequest): ResponseEntity<Any>? {
        val httpStatus = HttpStatus.UNPROCESSABLE_ENTITY
        return handleException(ex, "$httpStatus - ${ex.message}", httpStatus, HttpHeaders(), request)
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleAllOtherExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any>? {
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        return handleException(ex, "$httpStatus - ${ex.message}", httpStatus, HttpHeaders(), request)
    }

    private fun handleException(ex: Exception, message: String, httpStatus: HttpStatus, headers: HttpHeaders, request: WebRequest): ResponseEntity<Any>? {
        this.logger.error(message, ex)
        val body = ApiErrorResponse(httpStatus, message)
        return super.handleExceptionInternal(ex, body, headers, httpStatus, request)
    }
}

data class ApiErrorResponse(
    @Schema(description = "HttpStatus for responsen.")
    val httpStatus: HttpStatus,

    @Schema(description = "Beskrivelse av hva som har gått galt.")
    val melding: String?
)
