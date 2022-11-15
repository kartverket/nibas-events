package no.kartverket.nibas.config

import com.google.cloud.spring.pubsub.core.PubSubTemplate
import com.google.cloud.spring.pubsub.integration.AckMode
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.channel.DirectChannel
import org.springframework.messaging.MessageChannel


@Configuration
class NibasEventsConfig {

    @Value("\${nibas.pubsub.subscription:nibas-events-subscriber}")
    val subscriptionName: String = "nibas-events-subscriber"

    @Bean
    fun openApiDef(): OpenAPI {
        return OpenAPI().info(Info().title("NIBAS Events API").description("API for å hente events i Nasjonal inndelingsbase").version("v1"))
    }

    @Bean
    @ConditionalOnProperty(value = ["spring.cloud.gcp.pubsub.enabled"], havingValue = "true")
    fun inputMessageChannel() = DirectChannel()

    @Bean
    @ConditionalOnProperty(value = ["spring.cloud.gcp.pubsub.enabled"], havingValue = "true")
    fun inboundChannelAdapter(@Qualifier("inputMessageChannel") messageChannel: MessageChannel, pubSubTemplate: PubSubTemplate): PubSubInboundChannelAdapter {
        val adapter = PubSubInboundChannelAdapter(pubSubTemplate, subscriptionName)
        adapter.outputChannel = messageChannel
        adapter.ackMode = AckMode.MANUAL
        adapter.payloadType = String::class.java
        return adapter
    }
}
