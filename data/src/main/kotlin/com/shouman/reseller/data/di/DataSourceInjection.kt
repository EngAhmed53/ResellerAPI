package com.shouman.reseller.data.di


import com.shouman.reseller.data.datasource.FirebaseDataSource
import com.shouman.reseller.data.datasource.HandheldDataSource
import org.koin.dsl.module

object DataSourceInjection {
    val koinBean = module {
        single{ FirebaseDataSource() }
        single { HandheldDataSource() }
    }
}