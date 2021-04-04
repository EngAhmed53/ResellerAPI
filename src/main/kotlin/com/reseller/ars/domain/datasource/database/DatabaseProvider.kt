package com.reseller.ars.domain.datasource.database

interface DatabaseProvider {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
}