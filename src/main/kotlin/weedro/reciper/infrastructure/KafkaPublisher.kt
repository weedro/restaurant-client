package weedro.reciper.infrastructure

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import reactor.core.publisher.Mono
import weedro.reciper.domain.Recipe
import weedro.reciper.repository.RecipeRepository

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

    @Scheduled(fixedDelay = "\${app.generate-order-delay}")
    fun createOrder(): Mono<Recipe>? =
        recipeRepository.random()
            .map { kafkaPublisher.publishOrderCreateEvent(it) }
            .block()
            ?.onErrorReturn(Recipe())

}
