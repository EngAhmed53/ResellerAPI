package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repositories.CustomerRepository
import com.shouman.reseller.domain.repositories.RelationRepository

class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val relationRepository: RelationRepository
) : CustomerService {
    override fun createCustomer(customer: Customer, companyUID: String, branchId: Int, salesmanId: Int): Int {
        return customerRepository.createCustomer(companyUID, customer).also {
            relationRepository.createRelation(
                Relation(
                    type = EntityType.CUSTOMER,
                    companyId = companyUID,
                    branchId = branchId,
                    salesmanId = salesmanId,
                    customerId = it
                )
            )
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

    override fun getCompanyCustomers(uid: String, lastId: Int, size: Int): List<CompanyCustomer> {
        return customerRepository.getCompanyCustomers(uid, lastId, size)
    }

    override fun getBranchCustomers(branchId: Int, lastId: Int, size: Int): List<BranchCustomer> {
        return customerRepository.getBranchCustomers(branchId, lastId, size)
    }

    override fun getSalesmanCustomers(salesmanId: Int, lastId: Int, size: Int): List<SalesmanCustomer> {
        return customerRepository.getSalesmanCustomers(salesmanId, lastId, size)
    }

    override fun updateCustomerInfo(companyUID: String, customerId: Int, putCustomer: PutCustomer): Boolean {
        return customerRepository.updateCustomerInfo(companyUID, customerId, putCustomer)
    }

    override fun deleteCustomer(companyUID: String, customerId: Int): Boolean {
        return relationRepository.deleteRelation(companyUID, EntityType.CUSTOMER, customerId).also {
            customerRepository.deleteCustomer(companyUID, customerId)
        }
    }
}