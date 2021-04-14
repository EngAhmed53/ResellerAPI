package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Branch(
    val id: Int? = null,
    val name: String,
    val city: String,
    val country: String
)


@Serializable
data class PutBranch(
    val name: String? = null,
    val city: String? = null,
    val country: String? = null
)

@Serializable
data class ResponseBranch(
    val id: Int,
    val name: String,
    val city: String,
    val country: String
)
