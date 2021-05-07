package com.shouman.reseller.data.di


import com.shouman.reseller.data.datasource.database.dao.*
import org.koin.dsl.module

object DaoInjection {
    val koinBeans = module {
        single<RelationDao> { RelationDaoImpl }
        single<CompanyDao> { CompanyDaoImpl }
        single<BranchDao> { BranchDaoImpl }
        single<SalesmanDao> { SalesmanDaoImpl }
        single<CustomerDao> { CustomerDaoImpl }
    }
}