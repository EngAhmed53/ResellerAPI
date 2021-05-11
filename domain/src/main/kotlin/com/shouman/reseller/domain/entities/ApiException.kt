package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
class ApiException(val statusCode: StatusCode, val msg: String? = null): Exception(msg)