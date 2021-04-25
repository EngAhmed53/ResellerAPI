package com.shouman.reseller.di

import com.shouman.reseller.controller.BranchController
import com.shouman.reseller.controller.CompanyController
import com.shouman.reseller.controller.SalesmanController
import org.koin.dsl.module

object ControllerInjection {
    val koinBean = module {
        single { CompanyController(get()) }
        single { BranchController(get(), get()) }
        single { SalesmanController(get(), get(), get()) }
    }
}