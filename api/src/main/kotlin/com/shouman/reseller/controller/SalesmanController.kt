package com.shouman.reseller.controller

import com.shouman.reseller.Response
import com.shouman.reseller.domain.core.mappers.toResponseSalesman
import com.shouman.reseller.domain.core.mappers.toSalesmanItem
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.entities.StatusCode.*
import com.shouman.reseller.domain.services.*
import io.ktor.http.*
import org.koin.core.KoinComponent

class SalesmanController(
    private val salesmanService: SalesmanService,
    private val handheldService: HandheldService,
) : BaseController(), KoinComponent {

    private val isEmailUsed: (String) -> Boolean = { salesmanService.getSalesmanByEmail(it) != null }
    private val isSimNumberUsed: (String) -> Boolean = { salesmanService.getSalesmanBySimNumber(it) != null }
    private val isIMEIUsed: (Long) -> Boolean = { salesmanService.getSalesmanByIMEI(it) != null }

    suspend fun addSalesman(
        companyUID: String,
        branchId: Int,
        postSalesman: PostSalesman
    ): Response<Int> =
        dbQuery {
            val result = salesmanService.createSalesman(companyUID, branchId, postSalesman)
            return@dbQuery when (val status = result.first) {
                INVALID_RELATION -> {
                    HttpStatusCode.NotFound to ServerResponse(statusCode = status)
                }
                COMPANY_DISABLED -> HttpStatusCode.Locked to ServerResponse(statusCode = status)

                AUTH_ERROR, EMAIL_AUTH_ERROR, PHONE_NUMBER_AUTH_ERROR ->
                    HttpStatusCode.NotAcceptable to ServerResponse(statusCode = status)

                SUCCESS -> {
                    HttpStatusCode.Created to ServerResponse(body = result.second)
                }
                else -> throw Exception("Illegal status code ${result.first}")
            }
        }


    suspend fun getCompanySalesmen(
        companyUID: String,
        lastId: Int,
        size: Int
    ): Response<List<SalesmanBranch>> =
        dbQuery {
            val result = salesmanService.getCompanySalesmen(companyUID, lastId, size)
            return@dbQuery when (result.first) {
                INVALID_RELATION -> HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
                SUCCESS -> {
                    val salesmanList = result.second
                    HttpStatusCode.OK to ServerResponse(body = salesmanList)
                }
                else -> {
                    throw Exception("Illegal status code ${result.first}")
                }
            }
        }


    suspend fun getBranchSalesmen(
        companyUID: String,
        branchId: Int,
        lastId: Int,
        size: Int
    ): Response<List<SalesmanItem>> = dbQuery {
        val result = salesmanService.getBranchSalesmen(companyUID, branchId, lastId, size)
        return@dbQuery when (result.first) {
            INVALID_RELATION -> HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
            SUCCESS -> {
                val salesmanList = result.second.map { it.toSalesmanItem() }
                HttpStatusCode.OK to ServerResponse(body = salesmanList)
            }
            else -> {
                throw Exception("Illegal status code ${result.first}")
            }
        }
    }

    suspend fun getSalesmanById(
        salesmanId: Int
    ): Response<ResponseSalesman> = dbQuery {
        salesmanService.getSalesmanById(salesmanId)?.let {
            HttpStatusCode.OK to ServerResponse(body = it.toResponseSalesman())
        } ?: HttpStatusCode.NotFound to ServerResponse(statusCode = NOT_FOUND)
    }


    suspend fun isEmailExist(email: String): Response<Unit> = dbQuery {
        when (isEmailUsed.invoke(email)) {
            true -> HttpStatusCode.Conflict to ServerResponse(statusCode = EMAIL_ALREADY_TAKEN)
            false -> HttpStatusCode.OK to ServerResponse()
        }
    }

    suspend fun isSimNumberExist(number: String): Response<Unit> = dbQuery {
        when (isSimNumberUsed.invoke(number)) {
            true -> HttpStatusCode.Conflict to ServerResponse(statusCode = SIM_NUMBER_ALREADY_ASSIGNED)
            false -> HttpStatusCode.OK to ServerResponse()
        }
    }

    suspend fun isIMEIAccepted(imei: Long): Response<Unit> = dbQuery {
        when (isIMEIUsed.invoke(imei)) {
            true -> HttpStatusCode.Conflict to ServerResponse(statusCode = IMEI_ALREADY_ASSIGNED)// IMEI is already used, so it is not accepted
            false -> {
                when (handheldService.isHandheldModelAccepted(imei)) {
                    true -> HttpStatusCode.Accepted to ServerResponse()
                    false -> HttpStatusCode.NotAcceptable to ServerResponse(statusCode = DEVICE_NOT_SUPPORTED)
                }
            }
        }
    }

    suspend fun updateSalesman(
        companyUID: String,
        salesmanId: Int,
        putSalesman: PutSalesman
    ): Response<Unit> =
        dbQuery {
            when {
                putSalesman.email?.let { isEmailUsed(it) } == true -> HttpStatusCode.Conflict to ServerResponse(
                    statusCode = EMAIL_ALREADY_TAKEN
                )

                putSalesman.assignedDeviceIMEI?.let { isIMEIUsed(it) } == true -> HttpStatusCode.Conflict to ServerResponse(
                    statusCode = IMEI_ALREADY_ASSIGNED
                )

                putSalesman.assignedSimNumber?.let { isSimNumberUsed(it) } == true -> HttpStatusCode.Conflict to ServerResponse(
                    statusCode = SIM_NUMBER_ALREADY_ASSIGNED
                )

                else -> {
                    when (val status = salesmanService.updateSalesman(companyUID, salesmanId, putSalesman)) {
                        INVALID_RELATION -> HttpStatusCode.NotFound to ServerResponse(
                            statusCode = ERROR
                        )

                        COMPANY_DISABLED -> HttpStatusCode.Locked to ServerResponse(statusCode = COMPANY_DISABLED)

                        UPDATE_ERROR -> HttpStatusCode.BadRequest to ServerResponse(
                            UPDATE_ERROR
                        )

                        SUCCESS -> HttpStatusCode.OK to ServerResponse()

                        else -> throw Exception("Illegal status code $status")
                    }
                }
            }
        }

    suspend fun deleteSalesman(companyUID: String, salesmanId: Int): Response<Unit> = dbQuery {
        return@dbQuery when (val code = salesmanService.deleteSalesman(companyUID, salesmanId)) {
            INVALID_RELATION -> {
                HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
            }
            DELETE_ERROR -> {
                HttpStatusCode.BadRequest to ServerResponse(statusCode = DELETE_ERROR)
            }
            SUCCESS -> {
                HttpStatusCode.OK to ServerResponse(statusCode = SUCCESS)
            }
            else -> throw Exception("$code is not a valid state here")
        }
    }
}