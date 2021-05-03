package com.shouman.reseller.domain.services

interface HandheldService {
    fun isHandheldModelAccepted(imei: Long): Boolean
}