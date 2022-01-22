package weedro.reciper.domain

import io.micronaut.core.annotation.Introspected
import org.bson.types.ObjectId

@Introspected
data class Ingredient(var name: String, var quantity: Int) {
    // empty constructor for jackson
    constructor() : this("", 1)
}

@Introspected
data class Recipe(
    var id: ObjectId? = null,
    var hash: String,
    var prefix: String,
    var adjective: String,
    var icon: Int,
    var ingredients: List<Ingredient>
) {
    constructor() : this(null, "", "", "", 0, emptyList())
}
