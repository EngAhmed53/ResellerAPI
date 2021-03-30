package com.reseller.ars.domain.di

import com.reseller.ars.domain.datasource.database.DatabaseProvider
import com.reseller.ars.domain.datasource.database.H2DatabaseProvider
import org.koin.dsl.module

object DatabaseInjection {

    val koinBean = module {
        single<DatabaseProvider> { H2DatabaseProvider() }
    }
}