package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.repositories.FirebaseDbRepository

internal class FirebaseDbServiceImpl(
    private val firebaseDbRepository: FirebaseDbRepository
) : FirebaseDbService {
//    override fun pushCustomerItem(companyUid: String, salesmanUid: String, customerItem: CustomerItem) {
//        firebaseDbRepository.createCustomerItem(companyUid, salesmanUid, customerItem)
//    }

    override fun deleteCustomerItem(companyUid: String, salesmanUid: String, customerId: Int) {
        firebaseDbRepository.deleteCustomerItem(companyUid, salesmanUid, customerId)
    }
}