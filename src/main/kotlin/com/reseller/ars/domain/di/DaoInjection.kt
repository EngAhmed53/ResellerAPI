package com.reseller.ars.domain.di


import com.reseller.ars.domain.datasource.database.dao.CompanyDao
import com.reseller.ars.domain.datasource.database.dao.CompanyDaoImpl
import com.reseller.ars.domain.datasource.database.dao.RelationDao
import com.reseller.ars.domain.datasource.database.dao.RelationDaoImpl
import org.koin.dsl.module

object DaoInjection {
    val koinBeans = module {
        single<RelationDao> { RelationDaoImpl }
        single<CompanyDao> { CompanyDaoImpl }
    }
}