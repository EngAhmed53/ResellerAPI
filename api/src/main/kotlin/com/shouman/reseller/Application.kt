package com.shouman.reseller

import com.shouman.reseller.controller.BranchController
import com.shouman.reseller.controller.CompanyController
import com.shouman.reseller.controller.SalesmanController
import com.shouman.reseller.module.admin.adminCompanyRouting
import com.shouman.reseller.module.users.branchRouting
import com.shouman.reseller.module.users.companyRouting
import com.shouman.reseller.data.di.DaoInjection
import com.shouman.reseller.data.di.DatabaseInjection
import com.shouman.reseller.data.di.RepositoryInjection
import com.shouman.reseller.di.ControllerInjection
import com.shouman.reseller.domain.di.ServiceInjection
import com.shouman.reseller.domain.interfaces.DatabaseProvider
import com.shouman.reseller.module.users.adminSalesmanRouting
import io.ktor.routing.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.serialization.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.slf4j.event.Level

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.main(testing: Boolean = false) {

    install(CallLogging) {
        level = Level.DEBUG
    }

    install(Locations)

    install(Koin) {
        modules(
            module {

            },
            DatabaseInjection.koinBean,
            DaoInjection.koinBeans,
            RepositoryInjection.koinBean,
            ServiceInjection.koinBean,
            ControllerInjection.koinBean
        )
    }

    val databaseProvider by inject<DatabaseProvider>()

    databaseProvider.init()

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        val companyController: CompanyController by inject()
        val branchController: BranchController by inject()
        val salesmanController: SalesmanController by inject()

        adminCompanyRouting(companyController)
        companyRouting(companyController)
        branchRouting(branchController)
        adminSalesmanRouting(salesmanController)
    }
}

