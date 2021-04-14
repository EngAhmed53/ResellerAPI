package com.shouman.reseller.domain.core.mappers

import com.shouman.reseller.domain.entities.Branch
import com.shouman.reseller.domain.entities.ResponseBranch

fun Branch.toResponseBranch(): ResponseBranch = ResponseBranch(
    id = id ?: -1,
    name, city, country
)