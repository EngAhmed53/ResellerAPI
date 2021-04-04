package com.reseller.ars.app.core.extensions

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*


fun ApplicationCall.getCompanyUID(): String? = this.parameters["uid"]

fun ApplicationCall.getBranchId() : Int? = this.parameters["branchId"]?.toInt()