package com.shouman.reseller.domain.repositories

import com.shouman.reseller.domain.entities.*

interface CustomerRepository {
    fun createCustomer(companyUID: String, customer: Customer): Int

    fun getCustomerById(companyUID: String, customerId: Int): Customer?

    fun getCustomerByEmail(companyUID: String, email: String): Customer?

    fun getCustomerByPhoneNumber(companyUID: String, number: String): Customer?

    fun getCompanyCustomers(companyUID: String, lastId: Int, size: Int): List<CompanyCustomer>

    fun getBranchCustomers(branchId: Int, lastId: Int, size: Int): List<BranchCustomer>

    fun getSalesmanCustomers(salesmanId: Int, lastId: Int, size: Int): List<SalesmanCustomer>

    fun updateCustomerInfo(companyUID: String, customerId: Int, putCustomer: PutCustomer): Boolean

    fun deleteCustomer(companyUID: String, customerId: Int): Boolean
}