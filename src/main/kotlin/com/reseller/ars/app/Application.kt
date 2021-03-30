package com.reseller.ars.app

import com.reseller.ars.app.module.adminRouting
import com.reseller.ars.app.module.companyRouting
import com.reseller.ars.domain.datasource.database.DatabaseProvider
import com.reseller.ars.domain.di.ControllerInjection
import com.reseller.ars.domain.di.DaoInjection
import com.reseller.ars.domain.di.DatabaseInjection
import com.reseller.ars.domain.di.RepositoryInjection
import io.ktor.routing.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
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

    install(Koin) {
        modules(
            module {

            },
            DatabaseInjection.koinBean,
            DaoInjection.koinBeans,
            RepositoryInjection.koinBean,
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

        adminRouting()
        companyRouting()
    }
}

