package com.shouman.reseller.controller

import com.shouman.reseller.domain.core.mappers.toResponseCompany
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.entities.ResponseCode.*
import com.shouman.reseller.domain.services.CompanyService

class CompanyController(
    private val companyService: CompanyService
) : BaseController() {

    suspend fun createCompany(company: Company): Result<String> = dbQuery {
        companyService.getCompanyByUID(company.uid)?.let {
            return@dbQuery Result.Error(ApiException(COMPANY_UID_ALREADY_TAKEN))
        }

        val uid = companyService.createCompany(company)
        Result.Success(uid)
    }

    suspend fun getCompanyInfo(uid: String): Result<ResponseCompany> = dbQuery {
        companyService.getCompanyByUID(uid)?.run {
            Result.Success(toResponseCompany())
        } ?: Result.Error(ApiException(COMPANY_UID_INVALID))
    }

    suspend fun disableCompany(uid: String): Result<Boolean> = dbQuery {
        if (companyService.disableCompany(uid)) {
            Result.Success(true)
        } else {
            Result.Error(ApiException(COMPANY_DISABLED))
        }
    }

    suspend fun extendCompanyLicense(uid: String, license: License): Result<Boolean> = dbQuery {
        if (companyService.extendCompanyLicense(uid, license)) {
            Result.Success(true)
        } else {
            Result.Error(ApiException(COMPANY_LICENSE_EXTEND_ERROR))
        }
    }
}