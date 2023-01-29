package no.kartverket.nibas.inttest

import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventTarget
import no.kartverket.nibas.domain.EventType
import no.kartverket.nibas.events.EventService
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ActiveProfiles("security-off")
@Transactional
class EventServiceIntTest : TestWithDbContainer() {

    @Autowired
    private lateinit var eventService: EventService

    @Test
    @Throws(IOException::class)
    fun testPublishEndpoint() {

        val event = Event(
            id = 0,
            uuid = UUID.randomUUID().toString(),
            type = EventType.DELETED,
            target = EventTarget.GRUNNKRETS,
            targetId = UUID.randomUUID().toString(),
            timestamp = LocalDateTime.now(),
            gyldigFra = LocalDate.now(),
            gyldigTil = null
        )

        eventService.lagreEvent(event)

        val pageRequest = PageRequest.of( 0,  10)
        val findEventsBy = eventService.findEventsBy(pageRequest)
        assertThat(findEventsBy.size).isEqualTo(1)
        assertThat(findEventsBy[0].targetId).isEqualTo(event.targetId)
    }
}
