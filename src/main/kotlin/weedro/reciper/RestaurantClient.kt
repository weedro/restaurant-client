package weedro.reciper

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
            .args(*args)
            .packages("weedro.reciper")
            .start()
}
