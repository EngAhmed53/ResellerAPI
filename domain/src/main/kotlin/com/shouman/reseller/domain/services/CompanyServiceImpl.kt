package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.Company
import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.License
import com.shouman.reseller.domain.entities.Relation
import com.shouman.reseller.domain.repositories.CompanyRepository
import com.shouman.reseller.domain.repositories.RelationRepository

internal class CompanyServiceImpl(
    private val relationRepository: RelationRepository,
    private val companyRepository: CompanyRepository
) : CompanyService {

    override fun createCompany(company: Company): String {
        return companyRepository.createCompany(company).also {
            relationRepository.createRelation(Relation(EntityType.COMPANY, it))
        }
    }

    override fun getCompanyByUID(uid: String): Company? {
        return companyRepository.getCompanyByUID(uid)
    }

    override fun disableCompany(uid: String): Boolean {
        return companyRepository.disableCompany(uid)
    }

    override fun extendCompanyLicense(uid: String, license: License): Boolean {
        return companyRepository.extendCompanyLicense(uid, license)
    }

    override fun isCompanyEnabled(uid: String): Boolean {
        return companyRepository.isCompanyEnabled(uid)
    }
}