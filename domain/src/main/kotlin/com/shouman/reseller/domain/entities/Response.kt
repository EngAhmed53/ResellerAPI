package com.shouman.reseller.domain.entities

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseCode {
    SUCCESS,
    COMPANY_DISABLED,
    COMPANY_UID_ALREADY_TAKEN,
    COMPANY_UID_INVALID,
    COMPANY_LICENSE_EXTEND_ERROR,
    BRANCH_NOT_FOUND,
    BRANCH_UPDATE_ERROR,
    BRANCH_DELETE_ERROR,
    EMAIL_ALREADY_TAKEN,
    INVALID_RELATION,
    CUSTOMER_ALREADY_FOUND,
    SIM_NUMBER_ALREADY_ASSIGNED,
    IMEI_ALREADY_ASSIGNED,
    DEVICE_NOT_SUPPORTED,
    F_AUTH_ERROR,
    SALESMAN_CREATE_ERROR,
    SALESMAN_NOT_FOUND,
    SALESMAN_UPDATE_ERROR,
    SALESMAN_DELETE_ERROR,
    ERROR
}

@Serializable
data class ServerResponse<T>(
    val responseCode: ResponseCode = ResponseCode.SUCCESS,
    val msg: String? = null,
    val body: T? = null
)

