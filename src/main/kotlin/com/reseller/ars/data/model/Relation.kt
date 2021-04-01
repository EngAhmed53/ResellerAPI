package com.reseller.ars.data.model

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
    val branchId: String? = null,
    val salesmanId: String? = null,
    val customerId: String? = null,
    val invoiceId: String? = null
)

