package com.shouman.reseller.data.datasource.firebase

import com.google.firebase.database.FirebaseDatabase


class FirebaseDbDataSource {

    private val database by lazy { FirebaseDatabase.getInstance(FirebaseAppProvider.provideFirebaseApp()) }

//    fun createCustomerItem(companyUid: String, salesmanUid: String, customerItem: CustomerItem) {
//        val reference = database.getReference("CustomerItems")
//            .child(companyUid)
//            .child(salesmanUid)
//            .child(customerItem.id.toString())
//
//        reference.setValueAsync(customerItem)
//    }

    fun deleteCustomerItem(companyUid: String, salesmanUid: String, customerId: Int) {
        val reference = database.getReference("CustomerItems")
            .child(companyUid)
            .child(salesmanUid)
            .child(customerId.toString())

        reference.setValueAsync(null)
    }

}