package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*

interface CustomerService {

    fun createCustomer(
        newCustomer: PostCustomer,
        companyUID: String,
        branchId: Int,
        salesmanId: Int
    ): Pair<StatusCode, Int>

    fun getCustomerById(companyUID: String, customerId: Int): Customer?

    fun getCustomerByEmail(companyUID: String, email: String): Customer?

    fun getCustomerByPhoneNumber(companyUID: String, number: String): Customer?

    fun getCompanyCustomers(uid: String, lastId: Int, size: Int): Pair<StatusCode,List<CompanyCustomer>>

    fun getBranchCustomers(
        companyUID: String,
        branchId: Int,
        lastId: Int,
        size: Int
    ): Pair<StatusCode, List<BranchCustomer>>

    fun getSalesmanCustomers(
        companyUID: String,
        salesmanId: Int,
        lastId: Int,
        size: Int
    ): Pair<StatusCode, List<SalesmanCustomer>>

    fun updateCustomerInfo(companyUID: String, customerId: Int, putCustomer: PutCustomer): Boolean

    fun deleteCustomer(companyUID: String, customerId: Int): Boolean
}