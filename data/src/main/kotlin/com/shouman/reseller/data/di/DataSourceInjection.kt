package com.shouman.reseller.data.di


import com.shouman.reseller.data.datasource.firebase.FirebaseAuthDataSource
import com.shouman.reseller.data.datasource.HandheldDataSource
import com.shouman.reseller.data.datasource.firebase.FirebaseDbDataSource
import org.koin.dsl.module

object DataSourceInjection {
    val koinBean = module {
        single{ FirebaseAuthDataSource() }
        single { HandheldDataSource() }
        single { FirebaseDbDataSource() }
    }
}