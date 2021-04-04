package com.reseller.ars.data.model

import com.reseller.ars.domain.datasource.database.dao.CompanyDaoImpl
import com.reseller.ars.domain.datasource.database.dao.CompanyDaoImpl.default
import com.reseller.ars.domain.datasource.database.dao.CompanyDaoImpl.uniqueIndex
import kotlinx.serialization.Serializable


@Serializable
data class Salesman(
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

//fun Salesman.toResponseSalesman(): ResponseSalesman =
//    ResponseSalesman(
//        firstName = firstName,
//        lastName = lastName,
//        email = email,
//        nationalId = nationalId,
//        assignedSimNumber = assignedSimNumber,
//        assignedDeviceIMEI = assignedDeviceIMEI,
//        enabled = enabled
//    )

