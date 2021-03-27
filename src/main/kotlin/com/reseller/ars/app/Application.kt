package com.reseller.ars.app

import com.reseller.ars.data.di.DaoInjection
import com.reseller.ars.data.di.DatabaseInjection
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.locations.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.serialization.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.main(testing: Boolean = false) {

    install(Koin) {
        modules(
            module {

            },
            DatabaseInjection.koinBean,
            DaoInjection.koinBeans
        )
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

