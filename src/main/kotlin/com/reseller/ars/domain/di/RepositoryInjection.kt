package com.reseller.ars.domain.di

import com.reseller.ars.domain.repository.CompanyRepository
import com.reseller.ars.domain.repository.CompanyRepositoryImpl
import com.reseller.ars.domain.repository.RelationRepository
import com.reseller.ars.domain.repository.RelationRepositoryImpl
import org.koin.dsl.module

object RepositoryInjection {
    val koinBean = module {
        single<RelationRepository> { RelationRepositoryImpl() }
        single<CompanyRepository> { CompanyRepositoryImpl() }
    }
}