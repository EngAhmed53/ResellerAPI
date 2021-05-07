package com.shouman.reseller.data.repositoriesImpl

import com.shouman.reseller.data.datasource.database.dao.CustomerDao
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repositories.CustomerRepository

class CustomerRepositoryImpl(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override fun createCustomer(companyUID: String, customer: Customer): Int {
        return customerDao.insert(companyUID, customer)
    }

    override fun getCustomerById(companyUID: String, customerId: Int): Customer? {
        return customerDao.selectById(companyUID, customerId)
    }

    override fun getCustomerByEmail(companyUID: String, email: String): Customer? {
        return customerDao.selectByEmail(companyUID, email)
    }

    override fun getCustomerByPhoneNumber(companyUID: String, number: String): Customer? {
        return customerDao.selectByPhoneNumber(companyUID, number)
    }

    override fun getCompanyCustomers(companyUID: String, lastId: Int, size: Int): List<CompanyCustomer> {
        return customerDao.selectByCompanyUID(companyUID, lastId, size)
    }

    override fun getBranchCustomers(branchId: Int, lastId: Int, size: Int): List<BranchCustomer> {
        return customerDao.selectByBranchId(branchId, lastId, size)
    }

    override fun getSalesmanCustomers(salesmanId: Int, lastId: Int, size: Int): List<SalesmanCustomer> {
        return customerDao.selectBySalesmanId(salesmanId, lastId, size)
    }

    override fun updateCustomerInfo(companyUID: String, customerId: Int, putCustomer: PutCustomer): Boolean {
        return customerDao.update(companyUID, customerId, putCustomer)
    }

    override fun deleteCustomer(companyUID: String, customerId: Int): Boolean {
        return customerDao.delete(companyUID, customerId)
    }
}