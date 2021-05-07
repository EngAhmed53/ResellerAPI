package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class PostCustomer(
    val customerInfo: Customer,
    val location: Location,
    val visit: Visit
)