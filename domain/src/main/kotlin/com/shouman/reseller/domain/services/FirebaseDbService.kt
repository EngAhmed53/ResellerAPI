package com.shouman.reseller.domain.services

interface FirebaseDbService {
    //fun pushCustomerItem(companyUid: String, salesmanUid: String, customerItem: CustomerItem)
    fun deleteCustomerItem(companyUid: String, salesmanUid: String, customerId: Int)
}