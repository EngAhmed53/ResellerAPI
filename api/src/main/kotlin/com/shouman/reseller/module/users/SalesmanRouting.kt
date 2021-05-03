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

fun Route.salesmanRouting(salesmanController: SalesmanController) {

    post<SalesmenLocations.PostSalesman> { postSalesman ->

        val salesman = call.receive<PostSalesman>()

        val result = salesmanController.addSalesman(postSalesman.parent.uid, postSalesman.branchId, salesman)

        call.respond(result.toResponse())
    }

    get<SalesmenLocations.CompanyListing> { params ->

        val result = salesmanController.getCompanySalesmen(params.parent.uid, params.lastId, params.size)

        call.respond(ServerResponse(body = result))
    }

    get<SalesmenLocations.BranchListing> { params ->

        val result = salesmanController.getBranchSalesmen(params.branchId, params.lastId, params.size)

        call.respond(ServerResponse(body = result))
    }

    get<EmailCheck> { params ->

        val result = salesmanController.isEmailExist(email = params.email)

        call.respond(result.toResponse())
    }

    get<SimNumberCheck> { params ->

        val result = salesmanController.isSimNumberExist(number = params.number)

        call.respond(result.toResponse())
    }

    get<DeviceCheck> { params ->

        val result = salesmanController.isIMEIAccepted(imei = params.imei)

        call.respond(result.toResponse())
    }

    put {  }
}

@Location("api/users/companies/{uid}")
class SalesmenLocations(val uid: String) {

    @Location("branches/{branchId}/salesmen")
    data class PostSalesman(
        val parent: SalesmenLocations,
        val branchId: Int,
    )

    @Location("salesmen")
    data class CompanyListing(
        val parent: SalesmenLocations,
        val lastId: Int,
        val size: Int
    )

    @Location("branches/{branchId}/salesmen")
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

