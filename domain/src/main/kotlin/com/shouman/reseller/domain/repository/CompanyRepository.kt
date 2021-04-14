package com.shouman.reseller.domain.repository

import com.shouman.reseller.domain.entities.Company
import com.shouman.reseller.domain.entities.License

interface CompanyRepository {
    fun createCompany(company: Company): String

    fun getCompanyByUID(companyUID: String): Company?

    fun disableCompany(companyUID: String): Boolean

    fun extendCompanyLicense(companyUID: String, license: License): Boolean

    fun isCompanyEnabled(companyUID: String): Boolean
}