package com.shouman.reseller.data.datasource.database

import com.shouman.reseller.domain.interfaces.DatabaseProvider
import com.shouman.reseller.data.datasource.database.dao.BranchDaoImpl
import com.shouman.reseller.data.datasource.database.dao.CompanyDaoImpl
import org.jetbrains.exposed.sql.SchemaUtils.create
import com.shouman.reseller.data.datasource.database.dao.RelationDaoImpl
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
        Database.connect("jdbc:h2:~/home/reseller;AUTO_SERVER=TRUE", "org.h2.Driver")
        transaction {
            create(RelationDaoImpl, CompanyDaoImpl, BranchDaoImpl)
        }
    }

    override suspend fun <T> dbQuery(block: () -> T): T = withContext(dispatcher) {
        transaction { block() }
    }
}

