package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class LocationResponse(
    val id: Int,
    val latitude: Double,
    val longitude: Double
)

