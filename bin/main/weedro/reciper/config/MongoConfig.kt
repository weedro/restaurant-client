package weedro.reciper.config

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("mongo")
class MongoConfig {

    lateinit var collection: String

}
