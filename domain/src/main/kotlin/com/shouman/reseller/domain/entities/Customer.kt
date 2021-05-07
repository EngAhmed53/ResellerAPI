package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: Int? = null,
    val createdAt: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String
)

@Serializable
data class PutCustomer(
    val firstName: String?,
    val lastName: String?,
    val updatedAt: Long,
    val email: String?,
    val phoneNumber: String?,
)

@Serializable
data class CompanyCustomer(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val branchName: String,
    val salesmanFirstName: String,
    val salesmanLastName: String
)

@Serializable
data class BranchCustomer(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val salesmanFirstName: String,
    val salesmanLastName: String)

@Serializable
data class SalesmanCustomer(
    val id: Int,
    val firstName: String,
    val lastName: String,
)