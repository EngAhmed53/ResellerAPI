package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
enum class EntityType {
    COMPANY,
    BRANCH,
    SALESMAN,
    CUSTOMER,
    INVOICE
}

@Serializable
data class Relation(
    val type: EntityType,
    val companyId: String,
    val branchId: Int? = null,
    val salesmanId: Int? = null,
    val customerId: Int? = null,
    val invoiceId: Int? = null
)

