package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.core.mappers.toSalesman
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repositories.SalesmanRepository

internal class SalesmanServiceImpl(
    private val companyService: CompanyService,
    private val firebaseAuthService: FirebaseAuthService,
    private val relationService: RelationService,
    private val salesmanRepository: SalesmanRepository
) : SalesmanService {
    override fun createSalesman(companyUID: String, branchId: Int, postSalesman: PostSalesman): Pair<StatusCode, Int> {
        return when {
            relationService.isValidRelation(Relation(EntityType.BRANCH, companyUID, branchId))
                .not() -> StatusCode.INVALID_RELATION to -1

            companyService.isCompanyEnabled(companyUID).not() -> StatusCode.COMPANY_DISABLED to -1

            else -> {
                handleNewSalesman(companyUID, branchId, postSalesman)
            }
        }
    }

    private fun handleNewSalesman(
        companyUID: String,
        branchId: Int,
        postSalesman: PostSalesman
    ): Pair<StatusCode, Int> {
        var uid: String? = null
        when (val result = firebaseAuthService.createSalesmanFirebaseAccount(postSalesman)) {
            is Result.Success -> {
                uid = result.data.uid
            }
            is Result.Error -> {
                val errorMsg = result.exception.message
                errorMsg?.let {
                    if (it.contains("email", ignoreCase = true)) return StatusCode.EMAIL_AUTH_ERROR to -1
                    if (it.contains("phone number", ignoreCase = true)) StatusCode.PHONE_NUMBER_AUTH_ERROR to -1
                    else StatusCode.AUTH_ERROR to -1
                }
                return StatusCode.EMAIL_AUTH_ERROR to -1
            }
        }

        val salesman = postSalesman.toSalesman(uid)

        return try {
            val id = salesmanRepository.add(salesman).also {
                relationService.addRelation(
                    Relation(
                        type = EntityType.SALESMAN,
                        companyId = companyUID,
                        branchId = branchId,
                        salesmanId = it
                    )
                )
            }

            StatusCode.SUCCESS to id
        } catch (e: Exception) {
            firebaseAuthService.deleteSalesmanFirebaseAccount(uid)
            StatusCode.SALESMAN_CREATE_ERROR to -1
        }
    }

    override fun getSalesmanById(id: Int): Salesman? {
        return salesmanRepository.getSalesmanById(id)
    }

    override fun getSalesmanByEmail(email: String): Salesman? {
        return salesmanRepository.getSalesmanByEmail(email)
    }

    override fun getSalesmanBySimNumber(simNumber: String): Salesman? {
        return salesmanRepository.getSalesmanBySimNumber(simNumber)
    }

    override fun getSalesmanByIMEI(imei: Long): Salesman? {
        return salesmanRepository.getSalesmanByIMEI(imei)
    }

    override fun getBranchSalesmen(
        companyUID: String,
        branchId: Int,
        lastId: Int,
        size: Int
    ): Pair<StatusCode, List<Salesman>> {

        if (relationService.isValidRelation(
                Relation(
                    EntityType.BRANCH, companyId = companyUID, branchId = branchId
                )
            ).not()
        ) return StatusCode.INVALID_RELATION to emptyList()

        val salesmenList = salesmanRepository.getBranchSalesmen(branchId, lastId, size)

        return StatusCode.SUCCESS to salesmenList
    }

    override fun getCompanySalesmen(
        companyUID: String,
        lastId: Int,
        size: Int
    ): Pair<StatusCode, List<SalesmanBranch>> {
        val isValidRelation = relationService.isValidRelation(
            Relation(
                EntityType.COMPANY,
                companyId = companyUID,
            )
        )

        return if (isValidRelation) {
            StatusCode.SUCCESS to salesmanRepository.getCompanySalesmen(companyUID, lastId, size)
        } else {
            StatusCode.INVALID_RELATION to emptyList()
        }
    }

    override fun updateSalesman(
        companyUID: String,
        salesmanId: Int,
        putSalesman: PutSalesman
    ): StatusCode {
        return when {
            relationService.isValidRelation(
                Relation(
                    EntityType.SALESMAN,
                    companyId = companyUID,
                    salesmanId = salesmanId
                )
            ).not() -> StatusCode.INVALID_RELATION

            companyService.isCompanyEnabled(companyUID).not() -> StatusCode.COMPANY_DISABLED

            else -> {
                val isUpdated = salesmanRepository.updateSalesman(salesmanId, putSalesman)

                if (isUpdated) {
                    StatusCode.SUCCESS
                } else {
                    StatusCode.UPDATE_ERROR
                }
            }

        }
    }

    override fun deleteSalesman(companyUID: String, salesmanId: Int): StatusCode {
        if (relationService.isValidRelation(
                Relation(
                    EntityType.SALESMAN,
                    companyId = companyUID,
                    salesmanId = salesmanId
                )
            ).not()
        ) return StatusCode.INVALID_RELATION

        val isDeleted =
            salesmanRepository.takeIf { relationService.deleteRelation(companyUID, EntityType.SALESMAN, salesmanId) }
                ?.deleteSalesman(salesmanId) ?: return StatusCode.DELETE_ERROR

        return if (isDeleted) StatusCode.SUCCESS
        else StatusCode.DELETE_ERROR
    }
}