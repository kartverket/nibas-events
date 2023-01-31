package no.kartverket.nibas

import no.kartverket.nibas.inttest.TestWithDbContainer
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NibasEventsApplicationTests : TestWithDbContainer() {

	@Test
	fun contextLoads() {
	}
}
