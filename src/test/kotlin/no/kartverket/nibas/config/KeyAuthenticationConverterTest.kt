package no.kartverket.nibas.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import javax.servlet.http.HttpServletRequest
import org.mockito.Mockito.`when` as whenever

class KeyAuthenticationConverterTest {

    @BeforeEach
    fun beforeEach() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test That Converting Headers To Auth Matches A Known Principal`() {
        val principal = "Consumer"
        val apiKey = "apiKey"

        val httpServletRequest: HttpServletRequest = mock(HttpServletRequest::class.java)
        whenever(httpServletRequest.getHeader(eq("Authorization"))).thenReturn("Basic $apiKey")

        val kac = KeyAuthenticationConverter(mapOf(principal to {apiKey}))

        val authentication = kac.convert(httpServletRequest)
        assertThat(authentication?.principal).isEqualTo(principal)
    }

    @Test
    fun `test That Converting Headers To Auth Gives An Empty Auth When No Header`() {
        val httpServletRequest: HttpServletRequest = mock(HttpServletRequest::class.java)
        whenever(httpServletRequest.getHeader(eq("Authorization"))).thenReturn(null)



        val kac = KeyAuthenticationConverter(emptyMap())
        val authentication = kac.convert(httpServletRequest)
        assertThat(authentication).isNull()
    }

    @Test
    fun `test That Converting Headers To Auth Gives No Auth When Unknown ApiKey`() {
        val knownPrincipal = "Matrikkel"
        val knownApiKey = "apiKey"
        val unknownApiKey = "unknown apiKey"

        val httpServletRequest: HttpServletRequest = mock(HttpServletRequest::class.java)
        whenever(httpServletRequest.getHeader(eq("Authorization"))).thenReturn("Basic $unknownApiKey")

        val kac = KeyAuthenticationConverter(mapOf(knownPrincipal to {knownApiKey}))

        val authentication = kac.convert(httpServletRequest)
        assertThat(authentication).isNull()
    }
}
