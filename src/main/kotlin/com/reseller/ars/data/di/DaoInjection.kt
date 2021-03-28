package com.reseller.ars.data.di


import com.reseller.ars.data.database.dao.RelationDao
import com.reseller.ars.data.database.dao.RelationDaoImpl
import org.koin.dsl.module

object DaoInjection {
    val koinBeans = module {
        single<RelationDao> { RelationDaoImpl }
    }
}