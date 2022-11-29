package no.kartverket.nibas.repository

import kotlinx.coroutines.flow.Flow
import no.kartverket.nibas.domain.Event
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineSortingRepository

interface EventRepository : CoroutineSortingRepository<Event, Long> {
    fun findAllBy(pageable: Pageable): Flow<Event>
    suspend fun findByUuid(uuid: String): Event?
}
