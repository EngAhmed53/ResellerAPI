package com.reseller.ars.domain.di

import com.reseller.ars.domain.controller.CompanyController
import org.koin.dsl.module

object ControllerInjection {
    val koinBean = module {
        single { CompanyController() }
    }
}