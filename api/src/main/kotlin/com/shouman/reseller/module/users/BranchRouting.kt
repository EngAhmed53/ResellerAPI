package com.shouman.reseller.module.users

import com.shouman.reseller.controller.BranchController
import com.shouman.reseller.domain.core.mappers.toResponse
import com.shouman.reseller.domain.entities.Branch
import com.shouman.reseller.domain.entities.PutBranch
import com.shouman.reseller.domain.entities.ServerResponse
import com.typesafe.config.Optional
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.locations.*


@OptIn(KtorExperimentalLocationsAPI::class)
fun Routing.branchRouting(branchController: BranchController) {

    post("/api/users/company/{uid}/branch") {

        val uid = call.parameters["uid"] ?: return@post call.respond(
            status = HttpStatusCode.BadRequest, message = "Missing or malformed uid"
        )

        val branch = call.receive<Branch>()

        val result = branchController.creatCompanyBranch(uid, branch)

        call.respond(result.toResponse())
    }

    get<Branches.Listing> { path ->

        val result = branchController.getCompanyBranches(
            companyUID = path.parent.uid,
            lastId = path.lastId,
            size = path.size
        )

        call.respond(ServerResponse(body = result))

    }

    put<Branches.Edit> { path ->

        val putBranch = call.receive<PutBranch>()

        val result = branchController.updateBranchInfo(path.parent.uid, path.branchId, putBranch)

        call.respond(result.toResponse())
    }

    delete<Branches.Edit> { path ->

        val result = branchController.deleteCompanyBranchById(path.parent.uid, path.branchId)

        call.respond(result.toResponse())
    }
}


@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/api/users/company/{uid}")
class Branches(val uid: String) {

    @Location("/branches")
    data class Listing(
        val parent: Branches,
        val lastId: Int,
        val size: Int
    )

    @Location("/branch/{branchId}")
    data class Edit(
        val parent: Branches,
        val branchId: Int
    )
}