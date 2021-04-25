package com.shouman.reseller.core.extensions

import io.ktor.application.*


fun ApplicationCall.getCompanyUID(): String? = this.parameters["uid"]

fun ApplicationCall.getBranchId() : Int? = this.parameters["branchId"]?.toInt()