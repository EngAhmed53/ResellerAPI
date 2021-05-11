package com.shouman.reseller.data.datasource.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.shouman.reseller.domain.entities.PostSalesman
import com.shouman.reseller.domain.entities.Result
import com.shouman.reseller.domain.entities.SalesmanCredential
import org.apache.commons.lang3.RandomStringUtils
import java.lang.Exception
import kotlin.jvm.Throws

class FirebaseAuthDataSource {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance(FirebaseAppProvider.provideFirebaseApp()) }

    @Throws(FirebaseAuthException::class)
    fun createSalesmanFirebaseAccount(salesman: PostSalesman): Result<SalesmanCredential> {
        val password = RandomStringUtils.random(8, true, true)

        val request: UserRecord.CreateRequest = UserRecord.CreateRequest()
            .setEmail(salesman.email)
            .setEmailVerified(false)
            .setPassword(password)
            .setPhoneNumber(salesman.assignedSimNumber)
            .setDisplayName(salesman.firstName + salesman.lastName)
            .setDisabled(false)

        return try {
            val userRecord = firebaseAuth.createUser(request)
            val verificationLink = generateSalesmanEmailVerificationLink(userRecord.email)
            val credential = SalesmanCredential(userRecord.uid, userRecord.email, password, verificationLink)
            Result.Success(credential)
        } catch (e: FirebaseAuthException) {
            Result.Error(e)
        }
    }

    @Throws(FirebaseAuthException::class)
    fun generateSalesmanEmailVerificationLink(email: String): String {
        return firebaseAuth.generateEmailVerificationLink(email)
    }

    @Throws(FirebaseAuthException::class)
    fun deleteSalesmanFirebaseAccount(uid: String) {
        firebaseAuth.deleteUser(uid)
    }
}