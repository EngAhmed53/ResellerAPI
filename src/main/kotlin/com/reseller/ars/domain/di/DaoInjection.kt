package com.reseller.ars.domain.di


import com.reseller.ars.domain.datasource.database.dao.*
import org.koin.dsl.module

object DaoInjection {
    val koinBeans = module {
        single<RelationDao> { RelationDaoImpl }
        single<CompanyDao> { CompanyDaoImpl }
        single<BranchDao> { BranchDaoImpl }
    }
}