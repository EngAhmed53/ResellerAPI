package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.SalesmanCredential
import com.shouman.reseller.domain.repositories.FirebaseRepository


internal class FirebaseServiceImpl(
    private val firebaseRepository: FirebaseRepository
) : FirebaseService {

    override fun getSalesmanCredential(salesman: PostSalesman): SalesmanCredential {
        return firebaseRepository.getSalesmanFirebaseCredential(salesman)
    }

}