package com.shouman.reseller.domain.useCases

import com.shouman.reseller.domain.core.BaseUseCase
import com.shouman.reseller.domain.entities.Company
import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.License
import com.shouman.reseller.domain.entities.Relation
import com.shouman.reseller.domain.repository.CompanyRepository
import com.shouman.reseller.domain.repository.RelationRepository
import org.koin.core.inject

class CreateCompanyUseCase(
    private val company: Company
) : BaseUseCase<String> {

    private val companyRepository by inject<CompanyRepository>()
    private val relationRepository by inject<RelationRepository>()

    override fun invoke(): String {
        return companyRepository.createCompany(company).also {
            relationRepository.createRelation(Relation(EntityType.COMPANY, it))
        }
    }
}

class GetCompanyByUIDUseCase(
    private val uid: String
) : BaseUseCase<Company?> {

    private val companyRepository by inject<CompanyRepository>()

    override fun invoke(): Company? {
        return companyRepository.getCompanyByUID(uid)
    }
}

class DisableCompanyUseCase(
    private val uid: String
) : BaseUseCase<Boolean> {

    private val companyRepository by inject<CompanyRepository>()

    override fun invoke(): Boolean {
        return companyRepository.disableCompany(uid)
    }
}

class ExtendCompanyLicenseUseCase(
    private val uid: String,
    private val license: License
) : BaseUseCase<Boolean> {

    private val companyRepository by inject<CompanyRepository>()

    override fun invoke(): Boolean {
        return companyRepository.extendCompanyLicense(uid, license)
    }
}

class IsCompanyEnabledUseCase(
    private val uid: String
) : BaseUseCase<Boolean> {
    private val companyRepository by inject<CompanyRepository>()
    override fun invoke(): Boolean {
        return companyRepository.isCompanyEnabled(uid)
    }
}