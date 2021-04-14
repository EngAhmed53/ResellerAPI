package com.shouman.reseller.data.di

import com.shouman.reseller.data.datasource.database.H2DatabaseProvider
import com.shouman.reseller.domain.interfaces.DatabaseProvider
import org.koin.dsl.module

object DatabaseInjection {

    val koinBean = module {
        single<DatabaseProvider> { H2DatabaseProvider() }
    }
}