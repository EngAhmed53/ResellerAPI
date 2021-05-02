package com.shouman.reseller.domain.di

import com.shouman.reseller.domain.services.*
import org.koin.dsl.module

object ServiceInjection {
    val koinBean = module {
        single<CompanyService> { CompanyServiceImpl(get(), get()) }
        single<BranchUseService> { BranchUseServiceImpl(get(), get()) }
        single<SalesmanService> { SalesmanServiceImpl(get(), get()) }
        single<HandheldService> { HandheldServiceImpl(get()) }
        single<FirebaseService> { FirebaseServiceImpl(get()) }
    }
}