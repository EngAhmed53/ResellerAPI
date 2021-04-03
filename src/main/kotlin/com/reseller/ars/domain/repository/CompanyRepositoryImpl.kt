package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.License
import com.reseller.ars.domain.datasource.database.dao.CompanyDao
import org.koin.core.KoinComponent
import org.koin.core.inject

class CompanyRepositoryImpl : CompanyRepository, KoinComponent {

    private val companyDao by inject<CompanyDao>()

    override fun createCompany(company: Company): String {
        return companyDao.insertCompany(company)
    }

    override fun getCompanyByUID(companyUID: String): Company? {
        return companyDao.getCompanyByUID(companyUID)
    }

    override fun disableCompany(companyUID: String): Boolean {
        return companyDao.disableCompany(companyUID)
    }

    override fun extendCompanyLicense(companyUID: String, license: License): Boolean {
        return companyDao.extendCompanyLicense(companyUID, license)
    }

    override fun isCompanyEnabled(companyUID: String): Boolean {
        return companyDao.isCompanyEnabled(companyUID)
    }
}