package no.kartverket.nibas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
// Hello
@SpringBootApplication
class NibasEventsApplication

fun main(args: Array<String>) {
	runApplication<NibasEventsApplication>(*args)
}
