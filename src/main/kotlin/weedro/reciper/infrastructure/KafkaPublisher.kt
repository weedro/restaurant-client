package weedro.reciper.infrastructure

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Value
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import weedro.reciper.domain.Recipe
import weedro.reciper.repository.RecipeRepository
import java.time.Duration

@KafkaClient
interface KafkaPublisher {

    @Topic("\${app.kafka.topic.order-create}")
    fun publishOrderCreateEvent(recipe: Recipe): Mono<Recipe>

}

@Singleton
class OrderPublisher(
    private val recipeRepository: RecipeRepository,
    private val kafkaPublisher: KafkaPublisher
) {

    @Value("\${app.generate-order-delay}")
    lateinit var generateOrderDelay: String

    @EventListener
    fun createOrder(event: StartupEvent) =
        generateSequence(0) { it }.toFlux()
            .delayElements(Duration.parse(generateOrderDelay))
            .log("create random order")
            .map { recipeRepository.random() }
            .log("send random order")
            .flatMap { it }
            .flatMap { kafkaPublisher.publishOrderCreateEvent(it) }
            .blockLast()
}
