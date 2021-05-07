package com.shouman.reseller.domain.repositories

interface FirebaseDbRepository {
    //fun createCustomerItem(companyUid: String, salesmanUID: String, customerItem: CustomerItem)
    fun deleteCustomerItem(companyUid: String, salesmanUID: String, customerId: Int)
}