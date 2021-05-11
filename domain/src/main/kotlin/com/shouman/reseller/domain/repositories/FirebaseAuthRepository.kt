package com.shouman.reseller.domain.repositories

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.Result
import com.shouman.reseller.domain.entities.SalesmanCredential

interface FirebaseAuthRepository {
    fun getSalesmanFirebaseCredential(salesman: PostSalesman): Result<SalesmanCredential>

    fun deleteSalesmanFirebaseAccount(uid: String)

    fun getSalesmanVerificationLink(email: String): String
}