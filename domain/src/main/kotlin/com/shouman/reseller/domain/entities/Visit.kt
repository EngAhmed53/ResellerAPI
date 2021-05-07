package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Visit(
    val visitTime: Long
)