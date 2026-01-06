package no.kartverket.nibas.events

import com.fasterxml.jackson.databind.ObjectMapper
import no.kartverket.nibas.api.v1.common.EventType
import no.kartverket.nibas.api.v1.common.FlateType
import no.kartverket.nibas.api.v1.request.EventRadRequest
import no.kartverket.nibas.api.v1.request.EventRequest
import no.kartverket.nibas.api.v1.request.toEvent
import no.kartverket.nibas.config.NoSecurityConfig
import no.kartverket.nibas.config.WebSecurityConfig
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.time.LocalDate
import java.util.*

@ActiveProfiles("security-off")
@WebMvcTest(EventsController::class)
@ImportAutoConfiguration(WebSecurityConfig::class, NoSecurityConfig::class)
class EventsControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @MockitoBean
    private lateinit var eventService: EventService

    private val uri = "/v1/events"

    @Test
    fun publiserEventReturns201_Created() {
        val eventRequest =
            EventRequest(
                LocalDate.now(),
                setOf(
                    EventRadRequest(UUID.randomUUID().toString(), EventType.MODIFIED, FlateType.GRUNNKRETS, UUID.randomUUID().toString())
                )
            )
        whenever(eventService.lagreEvent(any())).thenReturn(eventRequest.toEvent())

        val postRequest = MockMvcRequestBuilders.post(uri)
        postRequest.content(mapper.writeValueAsString(eventRequest))

        val resultActions = mvc.perform(postRequest.contentType(MediaType.APPLICATION_JSON))
        val mvcResult = resultActions.andReturn()
        assertThat(mvcResult.response.status).isEqualTo(201)
    }

    @Test
    fun publiserEventUtenEventRadGir422_UNPROCESSABLE_ENTITY() {
        val eventRequest = EventRequest(LocalDate.now(), setOf())
        val postRequest = MockMvcRequestBuilders.post(uri)
        postRequest.content(mapper.writeValueAsString(eventRequest))

        val resultActions = mvc.perform(postRequest.contentType(MediaType.APPLICATION_JSON))
        val mvcResult = resultActions.andReturn()

        assertThat(mvcResult.response.status).isEqualTo(422)
    }

    @Test
    fun testHentEvents() {
        val event =
            EventRequest(
                LocalDate.now(),
                setOf(
                    EventRadRequest(UUID.randomUUID().toString(), EventType.MODIFIED, FlateType.GRUNNKRETS, UUID.randomUUID().toString())
                )
            ).toEvent()

        whenever(eventService.findEventsBy(PageRequest.of(0, 100))).thenReturn(listOf(event))
        whenever(eventService.getTotalAntallEvents()).thenReturn(1)

        val getRequest = MockMvcRequestBuilders.get(uri)
        val mvcResult = mvc.perform(getRequest).andReturn()

        assertThat(mvcResult.response.status).isEqualTo(200)
        assertThat(mvcResult.response.contentAsString).contains(event.eventRader.first().lokalId)
    }
}
