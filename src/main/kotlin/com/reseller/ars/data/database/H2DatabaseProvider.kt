package com.reseller.ars.data.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import kotlin.coroutines.CoroutineContext

class H2DatabaseProvider : DatabaseProvider, KoinComponent {

    private val dispatcher: CoroutineContext

    init {
        dispatcher = Dispatchers.IO
    }

    override fun init() {
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        transaction {
            //create()
        }
    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(dispatcher) {
        transaction { block() }
    }
}

