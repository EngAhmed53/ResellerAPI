package com.shouman.reseller.domain.di

import com.shouman.reseller.domain.services.*
import org.koin.dsl.module

object ServiceInjection {
    val koinBean = module {
        single<RelationService> { RelationServiceImpl(get()) }
        single<CompanyService> { CompanyServiceImpl(get(), get()) }
        single<BranchService> { BranchServiceImpl(get(), get(), get()) }
        single<SalesmanService> { SalesmanServiceImpl(get(), get(), get(), get()) }
        single<HandheldService> { HandheldServiceImpl(get(), get()) }
        single<FirebaseAuthService> { FirebaseAuthServiceImpl(get()) }
        single<CustomerService> { CustomerServiceImpl(get(), get(), get()) }
    }
}