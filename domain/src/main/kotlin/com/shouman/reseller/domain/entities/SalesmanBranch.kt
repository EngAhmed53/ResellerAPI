package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class SalesmanBranch(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val branchName: String,
    val enabled: Boolean
)
