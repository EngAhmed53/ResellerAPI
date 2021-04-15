package com.shouman.reseller.domain.core.mappers

import com.shouman.reseller.domain.entities.ResponseSalesman
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.SalesmanItem

internal fun Salesman.toResponseSalesman(): ResponseSalesman =
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

internal fun Salesman.toSalesmanItem(): SalesmanItem =
    SalesmanItem(
        id = id ?: -1,
        firstName = firstName,
        lastName = lastName,
        enabled = enabled
    )