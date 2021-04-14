package com.reseller.ars.domain.controller

import com.shouman.reseller.core.exception.CompanyException
import com.shouman.reseller.core.exception.SalesmanException
import com.shouman.reseller.domain.repository.BranchRepository
import com.shouman.reseller.domain.repository.CompanyRepository
import com.shouman.reseller.controller.BaseController
import com.shouman.reseller.domain.core.extensions.isNull
import com.shouman.reseller.domain.core.mappers.toResponseSalesman
import com.shouman.reseller.domain.core.mappers.toSalesmanItem
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repository.RelationRepository
import com.shouman.reseller.domain.repository.SalesmanRepository
import org.koin.core.KoinComponent
import org.koin.core.inject


class SalesmanController : BaseController(), KoinComponent {

    private val relationRepository by inject<RelationRepository>()
    private val companyRepository by inject<CompanyRepository>()
    private val salesmanRepository by inject<SalesmanRepository>()
    private val branchRepository by inject<BranchRepository>()


    suspend fun addSalesman(companyUID: String, branchId: Int, salesman: Salesman): Result<Int> {

        fun isEmailExist(email: String): Boolean =
            salesmanRepository.getSalesmanByEmail(email).isNull().not()

        fun isNationalIdExist(id: String): Boolean =
            salesmanRepository.getSalesmanByNationalId(id).isNull()

        fun isIMEIUsed(imei: Long): Boolean =
            salesmanRepository.getSalesmanByIMEI(imei).isNull()

        fun isSimNumberUsed(number: String): Boolean =
            salesmanRepository.getSalesmanBySimNumber(number).isNull()

        return dbQuery {

            when {
                companyRepository.isCompanyEnabled(companyUID)
                    .not() -> Result.Error(CompanyException("Company is disabled"))

                branchRepository.getBranchById(branchId)
                    .isNull() -> Result.Error(SalesmanException("Branch id is wrong"))

                isEmailExist(salesman.email) -> Result.Error(SalesmanException("email already used"))

                isNationalIdExist(salesman.nationalId) -> Result.Error(SalesmanException("nationalId already used"))

                isIMEIUsed(salesman.assignedDeviceIMEI) -> Result.Error(SalesmanException("imei already used"))

                isSimNumberUsed(salesman.assignedSimNumber) -> Result.Error(SalesmanException("sim number already used"))

                else -> {
                    val id = salesmanRepository.add(salesman)
                    relationRepository.createRelation(
                        Relation(
                            type = EntityType.SALESMAN,
                            companyId = companyUID,
                            branchId = branchId,
                            salesmanId = id
                        )
                    )
                    Result.Success(id)
                }
            }
        }
    }


    suspend fun getCompanySalesmen(companyUID: String, lastId: Int, size: Int): List<SalesmanBranch> = dbQuery {
        salesmanRepository.getCompanySalesmen(companyUID, lastId, size)
    }


    suspend fun getBranchSalesmen(branchId: Int, lastId: Int, size: Int): List<SalesmanItem> = dbQuery {
        val salesmen = salesmanRepository.getBranchSalesmen(branchId, lastId, size)
        salesmen.map { it.toSalesmanItem() }
    }


    suspend fun getSalesmanById(salesmanId: Int): Result<ResponseSalesman> = dbQuery {
        salesmanRepository.getSalesmanById(salesmanId)?.let {
            Result.Success(it.toResponseSalesman())
        } ?: Result.Error(SalesmanException("No salesman with this id -> id = $salesmanId"))
    }

    suspend fun getSalesmanByNationalId(nationalId: String): Result<ResponseSalesman> = dbQuery {
        salesmanRepository.getSalesmanByNationalId(nationalId)?.let {
            Result.Success(it.toResponseSalesman())
        } ?: Result.Error(SalesmanException("No salesman with this national id -> id = $nationalId"))
    }

    suspend fun getSalesmanByNEmail(nationalId: String): Result<ResponseSalesman> = dbQuery {
        salesmanRepository.getSalesmanByNationalId(nationalId)?.let {
            Result.Success(it.toResponseSalesman())
        } ?: Result.Error(SalesmanException("No salesman with this national id -> id = $nationalId"))
    }

    suspend fun getSalesmanBySimNumber(number: String): Result<ResponseSalesman> = dbQuery {
        salesmanRepository.getSalesmanBySimNumber(number)?.let {
            Result.Success(it.toResponseSalesman())
        } ?: Result.Error(SalesmanException("No salesman assigned to this sim number -> number = $number"))
    }

    suspend fun getSalesmanByIMEI(imei: Long): Result<ResponseSalesman> = dbQuery {
        salesmanRepository.getSalesmanByIMEI(imei)?.let {
            Result.Success(it.toResponseSalesman())
        } ?: Result.Error(SalesmanException("No salesman assigned to this IMEI -> imei = $imei"))
    }

    suspend fun updateSalesman(companyUID: String, salesmanId: Int, putSalesman: PutSalesman): Result<Boolean> =
        dbQuery {
            if (companyRepository.isCompanyEnabled(companyUID)) {
                when (salesmanRepository.updateSalesman(salesmanId, putSalesman)) {
                    true -> Result.Success(true)
                    else -> Result.Error(SalesmanException("Error updating salesman $salesmanId in company $companyUID"))
                }
            } else {
                Result.Error(CompanyException("Company is disabled"))
            }
        }

    suspend fun deleteSalesman(companyUID: String, salesmanId: Int): Result<Boolean> = dbQuery {
        if (salesmanRepository.deleteSalesman(salesmanId)) {
            relationRepository.deleteRelation(companyUID, EntityType.SALESMAN, salesmanId)
            Result.Success(true)
        } else {
            Result.Error(
                SalesmanException("Salesman $salesmanId in company $companyUID deletion failed, might be deleted already")
            )
        }
    }

}