package com.shouman.reseller.module.admin

import com.shouman.reseller.controller.CompanyController
import com.shouman.reseller.controller.SalesmanController
import com.shouman.reseller.domain.core.mappers.toResponse
import com.shouman.reseller.domain.entities.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject


/**
 * THE RESPONSE THIS WAY IS NEED TO BE TESTED ON AN ANDROID CLIENT
 */
fun Route.adminCompanyRouting(companyController: CompanyController) {

    route("api/admin/company") {

        put("{uid}/disable") {
            val uid = call.parameters["uid"] ?: return@put call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            call.respond(companyController.disableCompany(uid).toResponse())
        }

        post("{uid}/enable") {
            val uid = call.parameters["uid"] ?: return@post call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val license = call.receive<License>()

            call.respond(companyController.extendCompanyLicense(uid, license).toResponse())
        }
    }
}
