package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.License
import com.reseller.ars.domain.datasource.database.dao.CompanyDao
import org.koin.core.KoinComponent
import org.koin.core.inject

class CompanyRepositoryImpl : CompanyRepository, KoinComponent {

    private val companyDao by inject<CompanyDao>()

    override fun createCompany(company: Company): String {
        return companyDao.insert(company)
    }

    override fun getCompanyByUID(companyUID: String): Company? {
        return companyDao.selectById(companyUID)
    }

    override fun disableCompany(companyUID: String): Boolean {
        return companyDao.disable(companyUID)
    }

    override fun extendCompanyLicense(companyUID: String, license: License): Boolean {
        return companyDao.updateLicense(companyUID, license)
    }

    override fun isCompanyEnabled(companyUID: String): Boolean {
        return companyDao.isEnabled(companyUID)
    }
}