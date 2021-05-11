package com.shouman.reseller.data.datasource.database

import com.shouman.reseller.data.datasource.database.dao.*
import com.shouman.reseller.domain.interfaces.DatabaseProvider
import org.jetbrains.exposed.sql.SchemaUtils.create
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
        Database.connect("jdbc:h2:~/database/reseller;AUTO_SERVER=TRUE", "org.h2.Driver")
        transaction {
            create(RelationDaoImpl, CompanyDaoImpl, BranchDaoImpl, SalesmanDaoImpl, CustomerDaoImpl)
        }
    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(dispatcher) {
        transaction { block() }
    }
}

