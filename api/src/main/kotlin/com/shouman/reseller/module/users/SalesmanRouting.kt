package com.shouman.reseller.module.users

import com.shouman.reseller.controller.SalesmanController
import com.shouman.reseller.domain.core.mappers.toResponse
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.ServerResponse
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.adminSalesmanRouting(salesmanController: SalesmanController) {

    post<Salesmen.PostSalesman> { postSalesman ->

        val salesman = call.receive<Salesman>()

        val result = salesmanController.addSalesman(postSalesman.parent.uid, postSalesman.branchId, salesman)

        call.respond(result.toResponse())
    }

    get<Salesmen.CompanyListing> { params ->

        val result = salesmanController.getCompanySalesmen(params.parent.uid, params.lastId, params.size)

        call.respond(ServerResponse(body = result))
    }

    get<Salesmen.BranchListing> { params ->

        val result = salesmanController.getBranchSalesmen(params.branchId, params.lastId, params.size)

        call.respond(ServerResponse(body = result))
    }
}

@Location("api/users/companies/{uid}")
class Salesmen(val uid: String) {

    @Location("branches/{branchId}/salesmen")
     data class PostSalesman(
        val parent: Salesmen,
        val branchId: Int,
    )

    @Location("salesmen")
    data class CompanyListing(
        val parent: Salesmen,
        val lastId: Int,
        val size: Int
    )

    @Location("branch/{branchId}/salesmen")
    data class BranchListing(
        val parent: Salesmen,
        val branchId: Int,
        val lastId: Int,
        val size: Int
    )
}
