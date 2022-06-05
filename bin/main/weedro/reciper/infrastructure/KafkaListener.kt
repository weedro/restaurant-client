package weedro.reciper.infrastructure

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import reactor.core.publisher.Mono
import weedro.reciper.domain.Recipe
import weedro.reciper.repository.RecipeRepository

@KafkaListener
class KafkaListener(private val recipeRepository: RecipeRepository) {

    @Topic("\${app.kafka.topic.dish-prepare}")
    fun onDishPrepareEvent() {
        TODO("this microservice is not implement at this moment")
    }

    @Topic("\${app.kafka.topic.recipe-create}")
    fun onRecipeCreateEvent(recipe: Recipe): Mono<Boolean> =
        recipeRepository.save(recipe)

}
