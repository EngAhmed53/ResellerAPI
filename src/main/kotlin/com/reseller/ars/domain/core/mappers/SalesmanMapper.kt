package com.reseller.ars.domain.core.mappers

import com.reseller.ars.data.model.ResponseSalesman
import com.reseller.ars.data.model.Salesman

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
