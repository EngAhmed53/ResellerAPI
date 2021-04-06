package com.reseller.ars.domain.core.extensions


val Any.TAG: String
    get() = this.javaClass.simpleName

fun Any?.isNull(): Boolean = this == null

fun Any?.isOneOf(vararg target: Any?): Boolean {
    target.forEach {
        if (this == it) return true
    }

    return false
}