package com.reseller.ars.data.di

import com.reseller.ars.data.database.DatabaseProvider
import com.reseller.ars.data.database.H2DatabaseProvider
import org.koin.dsl.module

object DatabaseInjection {

    val koinBean = module {
        single<DatabaseProvider> { H2DatabaseProvider() }
    }
}