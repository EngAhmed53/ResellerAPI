package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
enum class StatusCode {
    SUCCESS,
    COMPANY_DISABLED,
    COMPANY_UID_ALREADY_TAKEN,
    UPDATE_ERROR,
    DELETE_ERROR,
    EMAIL_ALREADY_TAKEN,
    INVALID_RELATION,
    CUSTOMER_ALREADY_FOUND,
    SIM_NUMBER_ALREADY_ASSIGNED,
    IMEI_ALREADY_ASSIGNED,
    DEVICE_NOT_SUPPORTED,
    AUTH_ERROR,
    EMAIL_AUTH_ERROR,
    PHONE_NUMBER_AUTH_ERROR,
    SALESMAN_CREATE_ERROR,
    NOT_FOUND,
    ERROR
}

@Serializable
data class ServerResponse<T>(
    val statusCode: StatusCode = StatusCode.SUCCESS,
    val msg: String? = null,
    val body: T? = null
)

