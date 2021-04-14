package com.shouman.reseller.module.users

import com.shouman.reseller.controller.CompanyController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import com.shouman.reseller.domain.entities.Result


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