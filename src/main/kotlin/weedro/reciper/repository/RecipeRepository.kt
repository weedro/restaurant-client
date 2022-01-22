package weedro.reciper.repository

import com.mongodb.client.model.Aggregates
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.bson.types.ObjectId
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import weedro.reciper.config.MongoConfig
import weedro.reciper.domain.Recipe

interface RecipeRepository {

    fun random(): Mono<Recipe>

    fun save(recipe: Recipe): Mono<Boolean>

    @Singleton
    class Base(private val mongoConfig: MongoConfig, private val mongoClient: MongoClient) :
        RecipeRepository {

        override fun random(): Mono<Recipe> =
            getCollection()
                .aggregate(listOf(Aggregates.sample(1)))
                .toMono()

        // todo unique hash
        override fun save(recipe: Recipe): Mono<Boolean> {
            recipe.id = ObjectId(recipe.hash.substring(0..23))
            return Mono.from(getCollection().insertOne(recipe))
                .map { true }
                .log("save recipe: $recipe")
                .onErrorReturn(false)
        }

        private fun getCollection(): MongoCollection<Recipe> {
            return mongoClient.getDatabase(mongoConfig.collection)
                .getCollection(mongoConfig.collection, Recipe::class.java)
        }

    }
}
