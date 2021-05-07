package com.shouman.reseller.data.datasource.database.dao

import com.shouman.reseller.domain.entities.*

interface CustomerDao {

    fun insert(companyUID: String, customer: Customer): Int

    fun selectById(companyUID: String, customerId: Int): Customer?

    fun delete(companyUID: String, customerId: Int): Boolean

    fun selectByEmail(companyUID: String, email: String): Customer?

    fun selectByPhoneNumber(companyUID: String, number: String): Customer?

    fun selectByCompanyUID(uid: String, lastId: Int, size: Int): List<CompanyCustomer>

    fun selectByBranchId(id: Int, lastId: Int, size: Int): List<BranchCustomer>

    fun selectBySalesmanId(id: Int, lastId: Int, size: Int): List<SalesmanCustomer>

    fun update(companyUID: String, customerId: Int, putCustomer: PutCustomer): Boolean
}