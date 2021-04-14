package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val uid: String,
    val ownerName: String,
    val ownerId: String,
    val ownerMail: String,
    val ownerPhone: String,
    val name: String,
    val email: String?,
    val city: String,
    val country: String,
    val enabled: Boolean,
    val licenseExpire: Long
)
@Serializable
data class ResponseCompany(
    val ownerName: String,
    val ownerMail: String,
    val ownerPhone: String,
    val name: String,
    val email: String?,
    val city: String,
    val country: String,
    val enabled: Boolean,
    val licenseExpire: Long
)

//data class PutCompany(
//    val ownerName: String?,
//    val ownerPhone: String?,
//    val name: String?,
//    val email: String?,
//    val city: String?,
//    val country: String?,
//)


