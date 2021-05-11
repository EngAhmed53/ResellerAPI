package com.shouman.reseller.module.users

import com.shouman.reseller.controller.CustomerController
import com.shouman.reseller.domain.core.mappers.toResponse
import com.shouman.reseller.domain.entities.PostCustomer
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


@OptIn(KtorExperimentalLocationsAPI::class)
fun Routing.customerRouting(customerController: CustomerController) {

    post<CustomerLocations.Post> { params ->

        val postCustomer = call.receive<PostCustomer>()

        val result = with(params) {
            customerController.addCustomer(parent.uid, branchId, salesmanId, postCustomer)
        }

        call.respond(status = result.first, result.second)
    }

    get<CustomerLocations.CompanyListing> { params ->

        val result = with(params) {
            customerController.getCompanyCustomers(parent.uid, lastId, size)
        }

        call.respond(status = result.first, result.second)
    }

    get<CustomerLocations.BranchListing> { params ->

        val result = with(params) {
            customerController.getBranchCustomers(parent.uid, branchId, lastId, size)
        }

        call.respond(status = result.first, result.second)
    }

    get<CustomerLocations.SalesmanListing> { params ->

        val result = with(params) {
            customerController.getSalesmanCustomers(parent.uid, salesmanId, lastId, size)
        }

        call.respond(status = result.first, result.second)
    }

}

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/users/customers")
class CustomerLocations(val uid: String) {

    @Location("/")
    data class Post(
        val parent: CustomerLocations,
        val branchId: Int,
        val salesmanId: Int
    )

    @Location("/companyListing")
    data class CompanyListing(
        val parent: CustomerLocations,
        val lastId: Int,
        val size: Int
    )

    @Location("/branchListing")
    data class BranchListing(
        val parent: CustomerLocations,
        val branchId: Int,
        val lastId: Int,
        val size: Int
    )

    @Location("/salesmanListing")
    data class SalesmanListing(
        val parent: CustomerLocations,
        val salesmanId: Int,
        val lastId: Int,
        val size: Int
    )
}
