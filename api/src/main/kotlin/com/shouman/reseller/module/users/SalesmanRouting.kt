package com.shouman.reseller.module.users

import com.shouman.reseller.controller.SalesmanController
import com.shouman.reseller.domain.core.mappers.toResponse
import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.ServerResponse
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.salesmanRouting(salesmanController: SalesmanController) {

    post<SalesmenLocations.PostSalesman> { params ->

        val salesman = call.receive<PostSalesman>()


        val result = with(params) {
            salesmanController.addSalesman(parent.uid, branchId, salesman)
        }

        call.respond(status = result.first, message = result.second)
    }

    get<SalesmenLocations.CompanyListing> { params ->



        val result = with(params) {
            salesmanController.getCompanySalesmen(parent.uid, lastId, size)
        }


        call.respond(status = result.first, message = result.second)
    }

    get<SalesmenLocations.BranchListing> { params ->

        val result = with(params) {
            salesmanController.getBranchSalesmen(parent.uid, branchId, lastId, size)
        }

        call.respond(status = result.first, message = result.second)
    }

    get<EmailCheck> { params ->

        val result = with(params) {
            salesmanController.isEmailExist(email = email)
        }

        call.respond(status = result.first, message = result.second)
    }

    get<SimNumberCheck> { params ->

        val result = with(params) {
            salesmanController.isSimNumberExist(number = number)
        }

        call.respond(status = result.first, message = result.second)
    }

    get<DeviceCheck> { params ->

        val result = with(params) {
            salesmanController.isIMEIAccepted(imei = imei)
        }

        call.respond(status = result.first, message = result.second)
    }

    put {  }
}

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("api/users/salesmen")
class SalesmenLocations(val uid: String) {

    @Location("/")
    data class PostSalesman(
        val parent: SalesmenLocations,
        val branchId: Int,
    )

    @Location("/companyListing")
    data class CompanyListing(
        val parent: SalesmenLocations,
        val lastId: Int,
        val size: Int
    )

    @Location("/branchListing")
    data class BranchListing(
        val parent: SalesmenLocations,
        val branchId: Int,
        val lastId: Int,
        val size: Int
    )
}


@Location("api/users/checkEmailAvailability")
data class EmailCheck(
    val email:String
)

@Location("api/users/checkSimNumberAvailability")
data class SimNumberCheck(
    val number:String
)

@Location("api/users/checkDeviceType")
data class DeviceCheck(
    val imei:Long
)

