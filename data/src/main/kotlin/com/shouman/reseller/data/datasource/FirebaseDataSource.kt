package com.shouman.reseller.data.datasource


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.SalesmanCredential
import org.apache.commons.lang3.RandomStringUtils
import kotlin.jvm.Throws

class FirebaseDataSource {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    @Throws(FirebaseAuthException::class)
    fun createSalesmanFirebaseAccount(salesman: PostSalesman): SalesmanCredential{
        val password = RandomStringUtils.random(8, true, true);

        val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
            .setEmail(salesman.email)
            .setEmailVerified(false)
            .setPassword(password)
            .setPhoneNumber(salesman.assignedSimNumber)
            .setDisplayName(salesman.firstName + salesman.lastName)
            .setDisabled(false)

        val userRecord = FirebaseAuth.getInstance().createUser(request)

        return SalesmanCredential(userRecord.uid, salesman.email, password)
    }
}