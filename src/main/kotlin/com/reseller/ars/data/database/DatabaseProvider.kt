package com.reseller.ars.data.database

interface DatabaseProvider {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
}