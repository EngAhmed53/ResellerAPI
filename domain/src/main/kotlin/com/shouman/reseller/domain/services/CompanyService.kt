package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.Company
import com.shouman.reseller.domain.entities.License


interface CompanyService {
    fun createCompany(company: Company): String

    fun getCompanyByUID(uid: String): Company?

    fun disableCompany(uid: String): Boolean

    fun extendCompanyLicense(uid: String, license: License): Boolean

    fun isCompanyEnabled(uid: String): Boolean
}
