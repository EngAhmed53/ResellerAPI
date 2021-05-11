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

    route("api/admin/companies") {

        put("{uid}/disable") {
            val uid = call.parameters["uid"] ?: return@put call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val result = companyController.disableCompany(uid)

            call.respond(result)
        }

        post("{uid}/enable") {
            val uid = call.parameters["uid"] ?: return@post call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val license = call.receive<License>()

            val result = companyController.extendCompanyLicense(uid, license)

            call.respond(result)
        }
    }
}
