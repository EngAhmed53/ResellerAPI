package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.SalesmanCredential

interface FirebaseService {
    fun getSalesmanCredential(salesman: PostSalesman): SalesmanCredential
}