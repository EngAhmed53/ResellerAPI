package com.shouman.reseller.controller

import com.shouman.reseller.core.exception.CompanyException
import com.shouman.reseller.domain.core.mappers.toResponseCompany
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.useCases.CreateCompanyUseCase
import com.shouman.reseller.domain.useCases.DisableCompanyUseCase
import com.shouman.reseller.domain.useCases.ExtendCompanyLicenseUseCase
import com.shouman.reseller.domain.useCases.GetCompanyByUIDUseCase

class CompanyController(
    private val getCompanyByUIDUseCase: (String) -> Company? = { GetCompanyByUIDUseCase(it).invoke() },
    private val createCompanyUseCase: (Company) -> String = { CreateCompanyUseCase(it).invoke() },
    private val disableCompanyUseCase: (String) -> Boolean = { DisableCompanyUseCase(it).invoke() },
    private val extendCompanyLicenseUseCase: (String, License) -> Boolean = { uid, license ->
        ExtendCompanyLicenseUseCase(uid, license).invoke()
    }
) : BaseController() {

    suspend fun createCompany(company: Company): Result<String> = dbQuery {
        getCompanyByUIDUseCase.invoke(company.uid)?.let {
            return@dbQuery Result.Error(CompanyException("Company UID is already taken"))
        }

        val uid = createCompanyUseCase.invoke(company)
        Result.Success(uid)
    }

    suspend fun getCompanyInfo(uid: String): Result<ResponseCompany> = dbQuery {
        getCompanyByUIDUseCase.invoke(uid)?.run {
            Result.Success(toResponseCompany())
        } ?: Result.Error(CompanyException("No company with this uid -> uid = $uid"))
    }

    suspend fun disableCompany(uid: String): Result<Boolean> = dbQuery {
        if (disableCompanyUseCase.invoke(uid)) {
            Result.Success(true)
        } else {
            Result.Error(CompanyException("Error disabling company with uid = $uid"))
        }
    }

    suspend fun extendCompanyLicense(uid: String, license: License): Result<Boolean> = dbQuery {
        if (extendCompanyLicenseUseCase.invoke(uid, license)) {
            Result.Success(true)
        } else {
            Result.Error(CompanyException("Error extending company $uid license"))
        }
    }
}