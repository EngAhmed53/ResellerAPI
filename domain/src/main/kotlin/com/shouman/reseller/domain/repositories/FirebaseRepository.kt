package com.shouman.reseller.domain.repositories

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.SalesmanCredential

interface FirebaseRepository {
    fun getSalesmanFirebaseCredential(salesman: PostSalesman): SalesmanCredential
}