package com.shouman.reseller.controller

import com.shouman.reseller.Response
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.services.CustomerService
import com.shouman.reseller.domain.entities.StatusCode.*
import io.ktor.http.*

class CustomerController(
    private val customerService: CustomerService
) : BaseController() {
    suspend fun addCustomer(
        companyUID: String,
        branchId: Int,
        salesmanId: Int,
        newCustomer: PostCustomer
    ): Response<Int> = dbQuery {
        val result = customerService.createCustomer(newCustomer, companyUID, branchId, salesmanId)
        return@dbQuery when (result.first) {
            INVALID_RELATION -> {
                HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
            }
            COMPANY_DISABLED -> HttpStatusCode.Locked to ServerResponse(statusCode = COMPANY_DISABLED)

            CUSTOMER_ALREADY_FOUND -> HttpStatusCode.Conflict to ServerResponse(statusCode = CUSTOMER_ALREADY_FOUND)

            SUCCESS -> {
                HttpStatusCode.Created to ServerResponse(body = result.second)
            }
            else -> throw Exception("Illegal status code ${result.first}")
        }
    }

    suspend fun getCustomerById(companyUID: String, customerId: Int): Response<Customer> = dbQuery {
        customerService.getCustomerById(companyUID, customerId)?.let {
            HttpStatusCode.OK to ServerResponse(body = it)
        } ?: HttpStatusCode.NotFound to ServerResponse(statusCode = NOT_FOUND)
    }

    suspend fun getCompanyCustomers(companyUID: String, lastId: Int, size: Int): Response<List<CompanyCustomer>> =
        dbQuery {
            val result = customerService.getCompanyCustomers(companyUID, lastId, size)
            return@dbQuery when (result.first) {
                INVALID_RELATION -> HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
                SUCCESS -> HttpStatusCode.OK to ServerResponse(body = result.second)
                else -> throw Exception("Illegal status code ${result.first}")
            }
        }

    suspend fun getBranchCustomers(
        companyUID: String,
        branchId: Int,
        lastId: Int,
        size: Int
    ): Response<List<BranchCustomer>> =
        dbQuery {
            val result = customerService.getBranchCustomers(companyUID, branchId, lastId, size)
            return@dbQuery when (result.first) {
                INVALID_RELATION -> HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
                SUCCESS -> HttpStatusCode.OK to ServerResponse(body = result.second)
                else -> throw Exception("Illegal status code ${result.first}")
            }
        }

    suspend fun getSalesmanCustomers(
        companyUID: String,
        salesmanId: Int,
        lastId: Int,
        size: Int
    ): Response<List<SalesmanCustomer>> = dbQuery {
        val result = customerService.getSalesmanCustomers(companyUID, salesmanId, lastId, size)
        return@dbQuery when (result.first) {
            INVALID_RELATION -> HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
            SUCCESS -> HttpStatusCode.OK to ServerResponse(body = result.second)
            else -> throw Exception("Illegal status code ${result.first}")
        }
    }
}