package com.reseller.ars.domain.core.mappers

import com.reseller.ars.data.model.Branch
import com.reseller.ars.data.model.ResponseBranch


fun Branch.toResponseBranch(): ResponseBranch = ResponseBranch(
    id = id ?: -1,
    name, city, country
)