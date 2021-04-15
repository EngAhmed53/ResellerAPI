package com.shouman.reseller.module.users

import com.shouman.reseller.core.extensions.getBranchId
import com.shouman.reseller.core.extensions.getCompanyUID
import com.shouman.reseller.controller.BranchController
import com.shouman.reseller.domain.entities.Branch
import com.shouman.reseller.domain.entities.PutBranch
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import com.shouman.reseller.domain.entities.Result


fun Routing.branchRouting() {

    val branchController by inject<BranchController>()

    route("/company") {

        post("/{uid}/branch") {
            val companyUID = call.getCompanyUID() ?: return@post call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val branch = call.receive<Branch>()

            when (val result = branchController.creatCompanyBranch(companyUID, branch)) {
                is Result.Success -> {
                    call.respond(HttpStatusCode.Created, result.data)
                }
                is Result.Error -> {
                    call.respond(HttpStatusCode.Conflict, result.exception.message ?: "Error")
                }
            }

        }

        get("{uid}/branch") {

            val companyUID = call.getCompanyUID() ?: return@get call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val parameters = call.request.queryParameters
            val lastId = parameters["lastId"]?.toInt() ?: 0
            val size = parameters["size"]?.toInt() ?: 5

            val result = branchController.getCompanyBranches(companyUID, lastId = lastId, size = size)

            call.respond(HttpStatusCode.Created, result)

        }

        put("{uid}/branch/{branchId}") {
            val companyUID = call.getCompanyUID() ?: return@put call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val branchId = call.getBranchId() ?: return@put call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed branchId"
            )

            val putBranch = call.receive<PutBranch>()

            when (val result = branchController.updateBranchInfo(companyUID, branchId, putBranch)) {
                is Result.Success -> {
                    call.respond(HttpStatusCode.OK, result.data)
                }
                is Result.Error -> {
                    call.respond(HttpStatusCode.Conflict, result.exception.message ?: "Error")
                }
            }
        }

        delete("{uid}/branch/{branchId}") {
            val companyUID = call.getCompanyUID() ?: return@delete call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
            )

            val branchId = call.getBranchId() ?: return@delete call.respond(
                status = HttpStatusCode.BadRequest, message = "Missing or malformed branchId"
            )

            when (val result = branchController.deleteCompanyBranchById(companyUID, branchId)) {
                is Result.Success -> {
                    call.respond(HttpStatusCode.OK, result.data)
                }
                is Result.Error -> {
                    call.respond(HttpStatusCode.Conflict, result.exception.message ?: "Error")
                }
            }
        }
    }
}