package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.SalesmanCredential

interface FirebaseAuthService {
    fun createSalesmanFirebaseAccount(salesman: PostSalesman): SalesmanCredential
    fun deleteSalesmanFirebaseAccount(uid: String)
    fun generateSalesmanVerificationLink(email: String): String
}