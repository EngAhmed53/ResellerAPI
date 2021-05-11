package com.shouman.reseller.domain.core.mappers

import com.shouman.reseller.domain.entities.ApiException
import com.shouman.reseller.domain.entities.Result
import com.shouman.reseller.domain.entities.ServerResponse

fun <T: Any> Result<T>.toResponse(): ServerResponse<T> {

    return when(this) {
        is Result.Success -> ServerResponse(body = this.data)
        is Result.Error -> ServerResponse(statusCode = (exception as ApiException).statusCode, msg = exception.msg)
    }

}