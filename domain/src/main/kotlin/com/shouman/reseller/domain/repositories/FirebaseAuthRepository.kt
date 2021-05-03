package com.shouman.reseller.domain.repositories

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.SalesmanCredential

interface FirebaseAuthRepository {
    fun getSalesmanFirebaseCredential(salesman: PostSalesman): SalesmanCredential

    fun deleteSalesmanFirebaseAccount(uid: String)

    fun getSalesmanVerificationLink(email: String): String
}