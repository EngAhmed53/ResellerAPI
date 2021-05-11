package com.shouman.reseller.controller

import com.shouman.reseller.Response
import com.shouman.reseller.domain.core.mappers.toResponseCompany
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.entities.StatusCode.*
import com.shouman.reseller.domain.services.CompanyService
import io.ktor.http.*

class CompanyController(
    private val companyService: CompanyService
) : BaseController() {

    suspend fun createCompany(company: Company): HttpStatusCode = dbQuery {
        companyService.getCompanyByUID(company.uid)?.let {
            return@dbQuery HttpStatusCode.Conflict
        }

        companyService.createCompany(company)
        HttpStatusCode.Created
    }

    suspend fun getCompanyInfo(uid: String): Response<ResponseCompany> = dbQuery {
        companyService.getCompanyByUID(uid)?.run {
            HttpStatusCode.OK to ServerResponse(body = toResponseCompany())
        } ?: HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
    }

    suspend fun disableCompany(uid: String): HttpStatusCode = dbQuery {
        if (companyService.disableCompany(uid)) {
            HttpStatusCode.OK
        } else {
            HttpStatusCode.BadRequest
        }
    }

    suspend fun extendCompanyLicense(uid: String, license: License): HttpStatusCode = dbQuery {
        if (companyService.extendCompanyLicense(uid, license)) {
            HttpStatusCode.OK
        } else {
            HttpStatusCode.BadRequest
        }
    }
}