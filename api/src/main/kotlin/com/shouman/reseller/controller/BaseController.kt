package com.shouman.reseller.controller

import com.shouman.reseller.domain.interfaces.DatabaseProvider
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseController : KoinComponent {

    private val dbProvider by inject<DatabaseProvider>()

    suspend fun <T> dbQuery(block: () -> T): T {
        return dbProvider.dbQuery(block)
    }
}