package com.reseller.ars.app.module

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.License
import com.reseller.ars.data.model.Result
import com.reseller.ars.domain.controller.CompanyController
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject


fun Route.adminRouting() {

    val companyController by inject<CompanyController>()

    route("/admin/company") {
        post("/") {
            val company = call.receive<Company>()
            when (val result = companyController.createCompany(company)) {
                is Result.Success -> {
                    call.respond(HttpStatusCode.OK, result.data)
                }
                is Result.Error -> {
                    call.respond(HttpStatusCode.Conflict, result.exception.message ?: "Error")
                }
            }

        }

        put("/{uid}/disable") {
            val uid = call.parameters["uid"] ?: return@put call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            when (val result = companyController.disableCompany(uid)) {
                is Result.Success -> {
                    call.respond(status = HttpStatusCode.OK, "Company with uid = $uid is now disabled")
                }

                is Result.Error -> {
                    call.respond(status = HttpStatusCode.NotFound, result.exception.message ?: "Error")
                }
            }
        }

        post("{uid}/enable") {
            val uid = call.parameters["uid"] ?: return@post call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val license = call.receive<License>()

            when (val result = companyController.extendCompanyLicense(uid, license)) {
                is Result.Success -> {
                    call.respond(
                        status = HttpStatusCode.OK,
                        "Company with uid = $uid license is now extend up to ${license.licenseExpire}, and it's current status is ${license.isEnabled}"
                    )
                }

                is Result.Error -> {
                    call.respond(status = HttpStatusCode.NotFound, result.exception.message ?: "Error")
                }
            }

        }
    }
}