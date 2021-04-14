package com.shouman.reseller.di

import com.shouman.reseller.controller.BranchController
import com.shouman.reseller.controller.CompanyController
import org.koin.dsl.module

object ControllerInjection {
    val koinBean = module {
        single { CompanyController() }
        single { BranchController() }
    }
}