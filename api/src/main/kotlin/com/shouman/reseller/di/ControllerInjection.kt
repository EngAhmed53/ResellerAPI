package com.shouman.reseller.di

import com.shouman.reseller.controller.BranchController
import com.shouman.reseller.controller.CompanyController
import com.shouman.reseller.controller.CustomerController
import com.shouman.reseller.controller.SalesmanController
import org.koin.dsl.module

object ControllerInjection {
    val koinBean = module {
        single { CompanyController(get()) }
        single { BranchController(get()) }
        single { SalesmanController(get(), get()) }
        single { CustomerController(get()) }
    }
}