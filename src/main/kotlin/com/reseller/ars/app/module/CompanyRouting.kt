package com.reseller.ars.app.module

import com.reseller.ars.data.model.Result
import com.reseller.ars.domain.controller.CompanyController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject


fun Route.companyRouting() {

    val companyController by inject<CompanyController>()

    route("/company") {
        get("{uid}") {
            val uid = call.parameters["uid"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )
            when (val result = companyController.getCompanyInfo(uid)) {
                is Result.Success -> {
                    call.respond(status = HttpStatusCode.OK, result.data)
                }

                is Result.Error -> {
                    call.respond(status = HttpStatusCode.NotFound, result.exception.message ?: "Error")
                }
            }
        }
    }

}