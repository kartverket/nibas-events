package no.kartverket.nibas.inttest

import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventRad
import no.kartverket.nibas.domain.EventType
import no.kartverket.nibas.domain.FlateType
import no.kartverket.nibas.events.EventService
import no.kartverket.nibas.repository.EventRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@ActiveProfiles("security-off")
@Transactional
class EventServiceIntTest : TestWithDbContainer() {

    @Autowired
    private lateinit var eventService: EventService

    @Autowired
    private lateinit var radRepository: EventRadRepository

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Test
    fun testSaveEntity() {
        val newEvent = eventService.lagreEvent(buildEvent())

        val pageRequest = PageRequest.of( 0,  10)
        val findEventsBy = eventService.findEventsBy(pageRequest)
        val savedEvent = findEventsBy[0]
        assertThat(findEventsBy.size).isEqualTo(1)
        assertThat(savedEvent.id).isEqualTo(newEvent.id)
    }

    @Test
    fun testRaderIsSaved() {
        val newEvent = buildEvent()
        eventService.lagreEvent(newEvent)

        newEvent.eventRader.forEach {
            val eventrad = radRepository.findByUuid(it.uuid)
            assertThat(eventrad).isNotNull
        }
    }

    @Test
    fun testRaderIsDeleted() {
        val newEvent = eventService.lagreEvent(buildEvent())

        eventRepository.delete(newEvent)

        val pageRequest = PageRequest.of( 0,  10)
        val eventer = eventService.findEventsBy(pageRequest)
        assertThat(eventer).isEmpty()

        newEvent.eventRader.forEach {
            val rad = radRepository.findByUuid(it.uuid)
            assertThat(rad).isNull()
        }
    }
}

interface EventRadRepository : CrudRepository<EventRad, String> {
    fun findByUuid(id: String): EventRad?
}

fun buildEvent(): Event {
    return Event(
        id = 0,
        inntreffer = LocalDate.now(),
        timestamp = OffsetDateTime.now(),
        eventRader = setOf(
            buildEventRad()
        ),
    )
}

fun buildEventRad(): EventRad {
    return EventRad(
        uuid = UUID.randomUUID().toString(),
        eventType = EventType.MODIFIED,
        flateType = FlateType.GRUNNKRETS,
        lokalId = UUID.randomUUID().toString(),
        eventId = 0,
    )
}
