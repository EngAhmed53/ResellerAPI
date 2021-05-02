package com.shouman.reseller.data.repositoriesImpl

import com.shouman.reseller.data.datasource.FirebaseDataSource
import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.SalesmanCredential
import com.shouman.reseller.domain.repositories.FirebaseRepository

class FirebaseRepositoryImpl(
    private val firebaseDataSource: FirebaseDataSource
): FirebaseRepository {

    override fun getSalesmanFirebaseCredential(salesman: PostSalesman): SalesmanCredential {
        return firebaseDataSource.createSalesmanFirebaseAccount(salesman)
    }

}