package com.shouman.reseller.data.datasource

import io.ktor.client.*
import io.ktor.client.features.json.*

class HandheldDataSource {

    val client by lazy {
        HttpClient {
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
    }

    fun isHandheldTypeAccepted(imei: Long): Boolean {
        return true
    }

}