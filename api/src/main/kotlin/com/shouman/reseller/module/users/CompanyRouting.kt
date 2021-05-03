package com.shouman.reseller.module.users

import com.shouman.reseller.controller.CompanyController
import com.shouman.reseller.domain.core.mappers.toResponse
import com.shouman.reseller.domain.entities.Company
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import com.shouman.reseller.domain.entities.Result
import io.ktor.request.*


/**
 * Should use uid that generated form authentication
 */

fun Route.companyRouting(companyController: CompanyController) {

    route("api/users/companies") {

        post {
            val company = call.receive<Company>()
            call.respond(companyController.createCompany(company).toResponse())
        }

        get("{uid}") {
            val uid = call.parameters["uid"] ?: return@get call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )
            val result = companyController.getCompanyInfo(uid)

            call.respond(result.toResponse())
        }
    }
}