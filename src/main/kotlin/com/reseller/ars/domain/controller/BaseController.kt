package com.reseller.ars.domain.controller

import com.reseller.ars.domain.datasource.database.DatabaseProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseController : KoinComponent {

    private val dbProvider by inject<DatabaseProvider>()

    suspend fun <T> dbQuery(block: () -> T): T {
        return dbProvider.dbQuery(block)
    }
}