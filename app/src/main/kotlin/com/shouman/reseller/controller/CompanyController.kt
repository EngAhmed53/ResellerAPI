package com.shouman.reseller.controller

import com.shouman.reseller.core.exception.CompanyException
import com.shouman.reseller.domain.core.mappers.toResponseCompany
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repository.CompanyRepository
import com.shouman.reseller.domain.repository.RelationRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class CompanyController : BaseController(), KoinComponent {

    private val relationRepository by inject<RelationRepository>()
    private val companyRepository by inject<CompanyRepository>()

    suspend fun createCompany(company: Company): Result<String> = dbQuery {
        companyRepository.getCompanyByUID(company.uid)?.let {
            return@dbQuery Result.Error(CompanyException("Company UID is already taken"))
        }

        val uid = companyRepository.createCompany(company).also {
            relationRepository.createRelation(Relation(EntityType.COMPANY, it))
        }
        Result.Success(uid)
    }

    suspend fun getCompanyInfo(uid: String): Result<ResponseCompany> = dbQuery {
        companyRepository.getCompanyByUID(uid)?.run {
            Result.Success(toResponseCompany())
        } ?: Result.Error(CompanyException("No company with this uid -> uid = $uid"))
    }

    suspend fun disableCompany(uid: String): Result<Boolean> = dbQuery{
        if (companyRepository.disableCompany(uid)) {
            Result.Success(true)
        } else {
            Result.Error(CompanyException("Error disabling company with uid = $uid"), )
        }
    }

    suspend fun extendCompanyLicense(uid: String, license: License): Result<Boolean> = dbQuery {
        if (companyRepository.extendCompanyLicense(uid, license)) {
            Result.Success(true)
        } else {
            Result.Error(CompanyException("Error extending company license"))
        }
    }
}