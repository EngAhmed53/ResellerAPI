package com.shouman.reseller.domain.core.mappers

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.ResponseSalesman
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.SalesmanItem

fun PostSalesman.toSalesman(firebaseUID: String) =
    Salesman(
        id = null,
        uid = firebaseUID,
        firstName,lastName, email, assignedSimNumber, assignedDeviceIMEI, enabled
    )

fun Salesman.toResponseSalesman(): ResponseSalesman =
    ResponseSalesman(
        id = id ?: -1,
        firstName = firstName,
        lastName = lastName,
        email = email,
        assignedSimNumber = assignedSimNumber,
        assignedDeviceIMEI = assignedDeviceIMEI,
        enabled = enabled
    )

fun Salesman.toSalesmanItem(): SalesmanItem =
    SalesmanItem(
        id = id ?: -1,
        firstName = firstName,
        lastName = lastName,
        enabled = enabled
    )