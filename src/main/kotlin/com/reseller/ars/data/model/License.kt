package com.reseller.ars.data.model

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val isEnabled: Boolean = true,
    val licenseExpire: Long
)