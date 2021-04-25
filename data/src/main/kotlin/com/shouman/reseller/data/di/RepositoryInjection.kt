package com.shouman.reseller.data.di

import com.shouman.reseller.data.repositoriesImpl.BranchRepositoryImpl
import com.shouman.reseller.data.repositoriesImpl.CompanyRepositoryImpl
import com.shouman.reseller.data.repositoriesImpl.RelationRepositoryImpl
import com.shouman.reseller.data.repositoriesImpl.SalesmanRepositoryImpl
import com.shouman.reseller.domain.repositories.BranchRepository
import com.shouman.reseller.domain.repositories.CompanyRepository
import com.shouman.reseller.domain.repositories.RelationRepository
import com.shouman.reseller.domain.repositories.SalesmanRepository
import org.koin.dsl.module

object RepositoryInjection {
    val koinBean = module {
        single<RelationRepository> { RelationRepositoryImpl() }
        single<CompanyRepository> { CompanyRepositoryImpl() }
        single<BranchRepository> { BranchRepositoryImpl() }
        single<SalesmanRepository> { SalesmanRepositoryImpl() }
    }
}