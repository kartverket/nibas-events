package no.kartverket.nibas.repository

import no.kartverket.nibas.domain.Event
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository

interface EventRepository : PagingAndSortingRepository<Event, Long> {
    fun findAllBy(pageable: Pageable): List<Event>
}
