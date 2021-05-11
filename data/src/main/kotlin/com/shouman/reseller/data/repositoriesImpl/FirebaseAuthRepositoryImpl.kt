package com.shouman.reseller.data.repositoriesImpl

import com.shouman.reseller.data.datasource.firebase.FirebaseAuthDataSource
import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.Result
import com.shouman.reseller.domain.entities.SalesmanCredential
import com.shouman.reseller.domain.repositories.FirebaseAuthRepository

class FirebaseAuthRepositoryImpl(
    private val firebaseAuthDataSource: FirebaseAuthDataSource
): FirebaseAuthRepository {

    override fun getSalesmanFirebaseCredential(salesman: PostSalesman): Result<SalesmanCredential> {
        return firebaseAuthDataSource.createSalesmanFirebaseAccount(salesman)
    }

    override fun deleteSalesmanFirebaseAccount(uid: String) {
        firebaseAuthDataSource.deleteSalesmanFirebaseAccount(uid)
    }

    override fun getSalesmanVerificationLink(email: String): String {
       return firebaseAuthDataSource.generateSalesmanEmailVerificationLink(email)
    }
}