package no.kartverket.nibas.inttest

import no.kartverket.nibas.domain.Event
import no.kartverket.nibas.domain.EventRad
import no.kartverket.nibas.domain.EventType
import no.kartverket.nibas.domain.ObjektType
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
import java.time.LocalDateTime
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
        val newEvent = buildEvent()
        eventService.lagreEvent(newEvent)

        val pageRequest = PageRequest.of( 0,  10)
        val findEventsBy = eventService.findEventsBy(pageRequest)
        val savedEvent = findEventsBy[0]
        assertThat(findEventsBy.size).isEqualTo(1)
        assertThat(savedEvent.uuid).isEqualTo(newEvent.uuid)
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
        val newEvent = buildEvent()
        eventService.lagreEvent(newEvent)

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
    val eventId = UUID.randomUUID().toString()

    return Event(
        uuid = eventId,
        eventNummer = 0,
        inntreffer = LocalDate.now(),
        timestamp = LocalDateTime.now(),
        eventRader = setOf(
            buildEventRad(eventId)
        ),
    )
}

fun buildEventRad(eventId: String): EventRad {
    return EventRad(
        uuid = UUID.randomUUID().toString(),
        eventType = EventType.MODIFIED,
        objektType = ObjektType.GRUNNKRETS,
        lokalId = UUID.randomUUID().toString(),
        event = eventId,
    )
}
