package com.shouman.reseller.data.datasource.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream

object FirebaseAppProvider {
    private val firebaseApp by lazy {
        val serviceAccount = FileInputStream(
            "/home/ahmedshouman/IdeaProjects/ResellerAPI/reseller-firebase-adminsdk.json"
        )

        val databaseUrl = "https://reseller-3c93b-default-rtdb.firebaseio.com/"

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl(databaseUrl)
            .build()

        FirebaseApp.initializeApp(options)
    }

    fun provideFirebaseApp(): FirebaseApp = firebaseApp
}