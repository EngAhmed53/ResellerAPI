package com.reseller.ars.data.model
import io.ktor.http.*
import kotlinx.serialization.Serializable

enum class ResponseCode {
    SUCCESS,
    ERROR
}

@Serializable
data class Response<T>(
    val responseCode: ResponseCode,
    val data: T
)
