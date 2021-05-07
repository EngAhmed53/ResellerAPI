package com.shouman.reseller.controller

import com.shouman.reseller.domain.core.extensions.isNull
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.services.CompanyService
import com.shouman.reseller.domain.services.CustomerService
import com.shouman.reseller.domain.services.RelationService

class CustomerController(
    private val relationService: RelationService,
    private val companyService: CompanyService,
    private val customerService: CustomerService,
) : BaseController() {

    private val isEmailExist: (companyUID: String, email: String) -> Boolean = { uid, email ->
        customerService.getCustomerByEmail(companyUID = uid, email = email) != null
    }
    private val isPhoneNumberExist: (companyUID: String, number: String) -> Boolean = { uid, number ->
        customerService.getCustomerByPhoneNumber(companyUID = uid, number = number) != null
    }

    suspend fun addCustomer(
        companyUID: String,
        branchId: Int,
        salesmanId: Int,
        newCustomer: PostCustomer
    ): Result<Int> = dbQuery {
        when {

            relationService.isValidRelation(Relation(EntityType.SALESMAN, companyUID, branchId, salesmanId))
                .not() -> Result.Error(
                ApiException(
                    ResponseCode.INVALID_RELATION
                )
            )

            companyService.isCompanyEnabled(companyUID)
                .not() -> Result.Error(ApiException(ResponseCode.COMPANY_DISABLED))

            else -> {
                handleNewCustomer(newCustomer, companyUID, salesmanId, branchId)
            }
        }
    }

    private fun handleNewCustomer(
        postCustomer: PostCustomer,
        companyUID: String,
        salesmanId: Int,
        branchId: Int
    ): Result<Int> {
        val customerInfo = postCustomer.customerInfo
        when {
            isEmailExist.invoke(companyUID, customerInfo.email) ||
                    isPhoneNumberExist.invoke(
                        companyUID,
                        customerInfo.phoneNumber
                    ) -> {
                return Result.Error(ApiException(ResponseCode.CUSTOMER_ALREADY_FOUND))
            }
            else -> {
                val id = customerService.createCustomer(customerInfo, companyUID, branchId, salesmanId)
                // save the location

                // save the visit

                // notify other clients

                // return the saved id
                return Result.Success(id)
            }
        }
    }

    suspend fun getCustomerById(companyUID: String, customerId: Int): Result<Customer> = dbQuery {
        customerService.getCustomerById(companyUID, customerId)?.let {
            Result.Success(it)
        } ?: Result.Error(ApiException(ResponseCode.SALESMAN_NOT_FOUND))
    }

    suspend fun getCompanyCustomers(companyUID: String, lastId: Int, size: Int): List<CompanyCustomer> = dbQuery {
        customerService.getCompanyCustomers(companyUID, lastId, size)
    }

    suspend fun getBranchCustomers(companyUID: String, branchId: Int, lastId: Int, size: Int): List<BranchCustomer> =
        dbQuery {
            val isValidRelation = relationService.isValidRelation(
                Relation(
                    type = EntityType.BRANCH,
                    companyUID,
                    branchId = branchId
                )
            )

            return@dbQuery if (isValidRelation.not()) {
                emptyList()
            } else {
                customerService.getBranchCustomers(branchId, lastId, size)
            }
        }

    suspend fun getSalesmanCustomers(
        companyUID: String,
        branchId: Int,
        salesmanId: Int,
        lastId: Int,
        size: Int
    ): List<SalesmanCustomer> = dbQuery {

        val isValidRelation = relationService.isValidRelation(
            Relation(
                type = EntityType.SALESMAN,
                companyId = companyUID,
                branchId = branchId,
                salesmanId = salesmanId
            )
        )

        return@dbQuery if (isValidRelation.not()) {
            emptyList()
        } else {
            customerService.getSalesmanCustomers(salesmanId, lastId, size)
        }
    }
}