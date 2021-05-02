package com.shouman.reseller.data.di

import com.shouman.reseller.data.repositoriesImpl.*
import com.shouman.reseller.domain.repositories.*
import org.koin.dsl.module

object RepositoryInjection {
    val koinBean = module {
        single<RelationRepository> { RelationRepositoryImpl() }
        single<CompanyRepository> { CompanyRepositoryImpl() }
        single<BranchRepository> { BranchRepositoryImpl() }
        single<SalesmanRepository> { SalesmanRepositoryImpl() }
        single<FirebaseRepository> { FirebaseRepositoryImpl(get()) }
        single<HandheldRepository> { HandheldRepositoryImpl(get()) }
    }
}