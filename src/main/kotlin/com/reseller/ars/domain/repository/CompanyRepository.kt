package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.License

interface CompanyRepository {
    fun createCompany(company: Company): String

    fun getCompanyByUID(companyUID: String): Company?

    fun disableCompany(companyUID: String): Boolean

    fun extendCompanyLicense(companyUID: String, license: License): Boolean
}