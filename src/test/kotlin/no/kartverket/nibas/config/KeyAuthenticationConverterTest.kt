package no.kartverket.nibas.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import org.mockito.Mockito.`when` as whenever

class KeyAuthenticationConverterTest {

    @Mock
    private lateinit var serverWebExchangeMock: ServerWebExchange


    @BeforeEach
    fun beforeEach() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testThatConvertingHeadersToAuthMatchesAKnownPrincipal() {
        val principal = "Matrikkel"
        val apiKey = "apiKey"

        val serverHttpRequestMock: ServerHttpRequest = mock(ServerHttpRequest::class.java)
        val httpHeaders: HttpHeaders = mock(HttpHeaders::class.java)

        whenever(serverWebExchangeMock.request).thenReturn(serverHttpRequestMock)
        whenever(serverHttpRequestMock.headers).thenReturn(httpHeaders)
        whenever(httpHeaders["Authorization"]).thenReturn(mutableListOf("Basic $apiKey"))


        val kac = KeyAuthenticationConverter(mapOf(principal to {apiKey}))


        val authentication = kac.convert(serverWebExchangeMock).block()
        assertThat(authentication?.principal).isEqualTo(principal)
    }

    @Test
    fun testThatConveringHeadersToAuthGivesAnEmptyAuthWhenNoHeader() {
        val serverHttpRequestMock: ServerHttpRequest = mock(ServerHttpRequest::class.java)
        val httpHeaders: HttpHeaders = mock(HttpHeaders::class.java)

        whenever(serverWebExchangeMock.request).thenReturn(serverHttpRequestMock)
        whenever(serverHttpRequestMock.headers).thenReturn(httpHeaders)
        whenever(httpHeaders["Authorization"]).thenReturn(emptyList())

        val kac = KeyAuthenticationConverter(emptyMap())
        val authentication = kac.convert(serverWebExchangeMock).block()
        assertThat(authentication).isNull()
    }

    @Test
    fun testThatConvertingHeadersToAuthGivesNoAuthWhenUnknownApiKey() {
        val knownPrincipal = "Matrikkel"
        val knownApiKey = "apiKey"
        val unknownApiKey = "unknown apiKey"

        val serverHttpRequestMock: ServerHttpRequest = mock(ServerHttpRequest::class.java)
        val httpHeaders: HttpHeaders = mock(HttpHeaders::class.java)

        whenever(serverWebExchangeMock.request).thenReturn(serverHttpRequestMock)
        whenever(serverHttpRequestMock.headers).thenReturn(httpHeaders)
        whenever(httpHeaders["Authorization"]).thenReturn(mutableListOf("Basic $unknownApiKey"))


        val kac = KeyAuthenticationConverter(mapOf(knownPrincipal to {knownApiKey}))


        val authentication = kac.convert(serverWebExchangeMock).block()
        assertThat(authentication).isNull()
    }
}
