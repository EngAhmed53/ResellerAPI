package com.shouman.reseller.domain.core

import org.koin.core.KoinComponent

interface BaseUseCase<T> : KoinComponent {
    fun invoke(): T
}