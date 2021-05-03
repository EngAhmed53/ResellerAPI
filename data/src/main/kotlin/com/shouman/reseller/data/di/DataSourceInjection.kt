package com.shouman.reseller.data.di


import com.shouman.reseller.data.datasource.FirebaseAuthDataSource
import com.shouman.reseller.data.datasource.HandheldDataSource
import org.koin.dsl.module

object DataSourceInjection {
    val koinBean = module {
        single{ FirebaseAuthDataSource() }
        single { HandheldDataSource() }
    }
}