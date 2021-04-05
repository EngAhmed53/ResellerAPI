package com.reseller.ars.domain.di

import com.reseller.ars.domain.repository.*
import org.koin.dsl.module

object RepositoryInjection {
    val koinBean = module {
        single<RelationRepository> { RelationRepositoryImpl() }
        single<CompanyRepository> { CompanyRepositoryImpl() }
        single<BranchRepository> { BranchRepositoryImpl() }
        single<SalesmanRepository> { SalesmanRepositoryImpl() }
    }
}