package com.shouman.reseller.data.repositoriesImpl

import com.shouman.reseller.data.datasource.firebase.FirebaseDbDataSource
import com.shouman.reseller.domain.repositories.FirebaseDbRepository

class FirebaseDbRepositoryImpl(
    private val firebaseDbDataSource: FirebaseDbDataSource
) : FirebaseDbRepository {
//    override fun createCustomerItem(companyUid: String, salesmanUID: String, customerItem: CustomerItem) {
//        firebaseDbDataSource.createCustomerItem(companyUid, salesmanUID, customerItem)
//    }

    override fun deleteCustomerItem(companyUid: String, salesmanUID: String, customerId: Int) {
        firebaseDbDataSource.deleteCustomerItem(companyUid, salesmanUID, customerId)
    }
}