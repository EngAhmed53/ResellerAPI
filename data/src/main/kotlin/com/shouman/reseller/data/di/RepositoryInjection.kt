package com.shouman.reseller.data.di

import com.shouman.reseller.data.repositoriesImpl.BranchRepositoryImpl
import com.shouman.reseller.data.repositoriesImpl.CompanyRepositoryImpl
import com.shouman.reseller.data.repositoriesImpl.RelationRepositoryImpl
import com.shouman.reseller.data.repositoriesImpl.SalesmanRepositoryImpl
import com.shouman.reseller.domain.repository.BranchRepository
import com.shouman.reseller.domain.repository.CompanyRepository
import com.shouman.reseller.domain.repository.RelationRepository
import com.shouman.reseller.domain.repository.SalesmanRepository
import org.koin.dsl.module

object RepositoryInjection {
    val koinBean = module {
        single<RelationRepository> { RelationRepositoryImpl() }
        single<CompanyRepository> { CompanyRepositoryImpl() }
        single<BranchRepository> { BranchRepositoryImpl() }
        single<SalesmanRepository> { SalesmanRepositoryImpl() }
    }
}