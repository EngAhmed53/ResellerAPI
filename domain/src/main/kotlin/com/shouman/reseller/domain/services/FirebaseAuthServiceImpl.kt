package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.Result
import com.shouman.reseller.domain.entities.SalesmanCredential
import com.shouman.reseller.domain.repositories.FirebaseAuthRepository


internal class FirebaseAuthServiceImpl(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : FirebaseAuthService {

    override fun createSalesmanFirebaseAccount(salesman: PostSalesman): Result<SalesmanCredential> {
        return firebaseAuthRepository.getSalesmanFirebaseCredential(salesman)
    }

    override fun deleteSalesmanFirebaseAccount(uid: String) {
        firebaseAuthRepository.deleteSalesmanFirebaseAccount(uid)
    }

    override fun generateSalesmanVerificationLink(email: String): String {
        return firebaseAuthRepository.getSalesmanVerificationLink(email)
    }
}