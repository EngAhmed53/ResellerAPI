package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val isEnabled: Boolean = true,
    val licenseExpire: Long
)