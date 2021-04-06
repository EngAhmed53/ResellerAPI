package com.reseller.ars.domain.core.mappers

import com.reseller.ars.data.model.ResponseSalesman
import com.reseller.ars.data.model.Salesman
import com.reseller.ars.data.model.SalesmanItem

fun Salesman.toResponseSalesman(): ResponseSalesman =
    ResponseSalesman(
        id = id ?: -1,
        firstName = firstName,
        lastName = lastName,
        email = email,
        nationalId = nationalId,
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