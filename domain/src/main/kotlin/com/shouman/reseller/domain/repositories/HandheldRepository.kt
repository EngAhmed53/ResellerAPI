package com.shouman.reseller.domain.repositories

interface HandheldRepository {
    fun isHandheldTypeAccepted(imei: Long): Boolean
}