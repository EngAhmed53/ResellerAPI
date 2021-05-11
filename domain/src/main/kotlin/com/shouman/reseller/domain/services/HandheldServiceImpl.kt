package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.repositories.HandheldRepository

internal class HandheldServiceImpl(
    private val salesmanService: SalesmanService,
    private val handheldRepository: HandheldRepository
) : HandheldService {

    override fun isHandheldModelAccepted(imei: Long): Boolean {
        return handheldRepository.isHandheldTypeAccepted(imei = imei)
    }
}