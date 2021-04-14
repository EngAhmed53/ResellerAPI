package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable


@Serializable
data class Salesman(
    val id: Int? = null,
    val uid: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val nationalId: String,
    val assignedSimNumber: String,
    val assignedDeviceIMEI: Long,
    val enabled: Boolean
)

@Serializable
data class PutSalesman(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val nationalId: String? = null,
    val assignedSimNumber: String? = null,
    val assignedDeviceIMEI: Long? = null,
    val enabled: Boolean? = null
)

@Serializable
data class ResponseSalesman(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val nationalId: String,
    val assignedSimNumber: String,
    val assignedDeviceIMEI: Long,
    val enabled: Boolean
)

@Serializable
data class SalesmanItem(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val enabled: Boolean
)