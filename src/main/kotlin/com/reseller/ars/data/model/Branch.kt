package com.reseller.ars.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Branch(
    val name: String,
    val city:String,
    val country: String
)


@Serializable
data class PutBranch(
    val name: String?,
    val city:String?,
    val country: String?
)
