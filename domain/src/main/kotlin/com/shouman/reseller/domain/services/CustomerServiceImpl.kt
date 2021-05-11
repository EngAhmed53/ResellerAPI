package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repositories.CustomerRepository
import com.shouman.reseller.domain.repositories.RelationRepository

class CustomerServiceImpl(
    private val relationService: RelationService,
    private val companyService: CompanyService,
    private val customerRepository: CustomerRepository
) : CustomerService {

    private val isEmailExist: (companyUID: String, email: String) -> Boolean = { uid, email ->
        getCustomerByEmail(companyUID = uid, email = email) != null
    }
    private val isPhoneNumberExist: (companyUID: String, number: String) -> Boolean = { uid, number ->
        getCustomerByPhoneNumber(companyUID = uid, number = number) != null
    }

    override fun createCustomer(
        newCustomer: PostCustomer,
        companyUID: String,
        branchId: Int,
        salesmanId: Int
    ): Pair<StatusCode, Int> {
        return when {
            relationService.isValidRelation(Relation(EntityType.SALESMAN, companyUID, branchId, salesmanId))
                .not() -> StatusCode.INVALID_RELATION to -1

            companyService.isCompanyEnabled(companyUID)
                .not() -> StatusCode.COMPANY_DISABLED to -1

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
    ): Pair<StatusCode, Int> {
        val customerInfo = postCustomer.customerInfo
        when {
            isEmailExist.invoke(companyUID, customerInfo.email) ||
                    isPhoneNumberExist.invoke(
                        companyUID,
                        customerInfo.phoneNumber
                    ) -> {
                return StatusCode.CUSTOMER_ALREADY_FOUND to -1
            }
            else -> {
                val id = customerRepository.createCustomer(companyUID, customerInfo).also {
                    relationService.addRelation(
                        Relation(
                            type = EntityType.CUSTOMER,
                            companyId = companyUID,
                            branchId = branchId,
                            salesmanId = salesmanId,
                            customerId = it
                        )
                    )
                }
                // save the location

                // save the visit

                // notify other clients

                // return the saved id
                return StatusCode.SUCCESS to id
            }
        }
    }

    override fun getCustomerById(companyUID: String, customerId: Int): Customer? {
        return customerRepository.getCustomerById(companyUID, customerId)
    }

    override fun getCustomerByEmail(companyUID: String, email: String): Customer? {
        return customerRepository.getCustomerByEmail(companyUID, email)
    }

    override fun getCustomerByPhoneNumber(companyUID: String, number: String): Customer? {
        return customerRepository.getCustomerByPhoneNumber(companyUID, number)
    }

    override fun getCompanyCustomers(uid: String, lastId: Int, size: Int): Pair<StatusCode, List<CompanyCustomer>> {

        val isValidRelation = relationService.isValidRelation(
            Relation(
                type = EntityType.COMPANY,
                uid,
            )
        )

        return if (isValidRelation) {
            StatusCode.SUCCESS to customerRepository.getCompanyCustomers(uid, lastId, size)
        } else {
            StatusCode.INVALID_RELATION to emptyList()
        }
    }

    override fun getBranchCustomers(
        companyUID: String,
        branchId: Int,
        lastId: Int,
        size: Int
    ): Pair<StatusCode, List<BranchCustomer>> {
        val isValidRelation = relationService.isValidRelation(
            Relation(
                type = EntityType.BRANCH,
                companyUID,
                branchId = branchId
            )
        )

        return if (isValidRelation) {
            StatusCode.SUCCESS to customerRepository.getBranchCustomers(branchId, lastId, size)
        } else {
            StatusCode.INVALID_RELATION to emptyList()
        }
    }

    override fun getSalesmanCustomers(
        companyUID: String,
        salesmanId: Int,
        lastId: Int,
        size: Int
    ): Pair<StatusCode, List<SalesmanCustomer>> {

        val isValidRelation = relationService.isValidRelation(
            Relation(
                type = EntityType.SALESMAN,
                companyUID,
                salesmanId = salesmanId
            )
        )

        return if (isValidRelation) {
            StatusCode.SUCCESS to customerRepository.getSalesmanCustomers(salesmanId, lastId, size)
        } else {
            StatusCode.INVALID_RELATION to emptyList()
        }
    }

    override fun updateCustomerInfo(companyUID: String, customerId: Int, putCustomer: PutCustomer): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteCustomer(companyUID: String, customerId: Int): Boolean {
        TODO("Not yet implemented")
    }
}