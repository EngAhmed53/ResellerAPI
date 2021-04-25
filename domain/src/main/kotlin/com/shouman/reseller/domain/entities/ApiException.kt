package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
class ApiException(val responseCode: ResponseCode): Exception()