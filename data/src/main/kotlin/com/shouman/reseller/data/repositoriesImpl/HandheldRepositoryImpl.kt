package com.shouman.reseller.data.repositoriesImpl

import com.shouman.reseller.data.datasource.HandheldDataSource
import com.shouman.reseller.domain.repositories.HandheldRepository

class HandheldRepositoryImpl(
    private val handheldDataSource: HandheldDataSource
) : HandheldRepository {

    override fun isHandheldTypeAccepted(imei: Long): Boolean {
        return handheldDataSource.isHandheldTypeAccepted(imei)
    }
}