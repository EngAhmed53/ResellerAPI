package com.shouman.reseller.domain.interfaces

interface DatabaseProvider {
    fun init()
    suspend fun <T> dbQuery(block: () -> T): T
}