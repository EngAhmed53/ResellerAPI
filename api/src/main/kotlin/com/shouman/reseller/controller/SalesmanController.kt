package com.shouman.reseller.controller

import com.shouman.reseller.domain.core.extensions.isNull
import com.shouman.reseller.domain.core.mappers.toResponseSalesman
import com.shouman.reseller.domain.core.mappers.toSalesmanItem
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.entities.ResponseCode.*
import com.shouman.reseller.domain.services.*
import org.koin.core.KoinComponent

class SalesmanController(
    private val companyService: CompanyService,
    private val salesmanService: SalesmanService,
    private val branchUseService: BranchUseService,
) : BaseController(), KoinComponent {

    private val isEmailUsed: (String) -> Boolean = { salesmanService.getSalesmanByEmail(it) != null }
    private val isSimNumberUsed: (String) -> Boolean = { salesmanService.getSalesmanBySimNumber(it) != null }
    private val isNationalIdUsed: (String) -> Boolean = { salesmanService.getSalesmanByNationalId(it) != null }
    private val isIMEIUsed: (Long) -> Boolean = { salesmanService.getSalesmanByIMEI(it) != null }

    suspend fun addSalesman(companyUID: String, branchId: Int, salesman: Salesman): Result<Int> =
        dbQuery {

            when {
                companyService.isCompanyEnabled(companyUID).not() -> Result.Error(ApiException(COMPANY_DISABLE))

                branchUseService.getBranchById(branchId).isNull() -> Result.Error(ApiException(BRANCH_NOT_FOUND))

                isEmailUsed(salesman.email) -> Result.Error(ApiException(EMAIL_ALREADY_TAKEN))

                isNationalIdUsed(salesman.nationalId) -> Result.Error(ApiException(NATIONAL_ID_ALREADY_TAKEN))

                isIMEIUsed(salesman.assignedDeviceIMEI) -> Result.Error(ApiException(IMEI_ALREADY_ASSIGNED))

                isSimNumberUsed(salesman.assignedSimNumber) -> Result.Error(ApiException(SIM_NUMBER_ALREADY_ASSIGNED))

                else -> {
                    val id = salesmanService.createSalesman(companyUID, branchId, salesman)
                    Result.Success(id)
                }
            }
        }


    suspend fun getCompanySalesmen(companyUID: String, lastId: Int, size: Int): List<SalesmanBranch> = dbQuery {
        salesmanService.getCompanySalesmen(companyUID, lastId, size)
    }


    suspend fun getBranchSalesmen(branchId: Int, lastId: Int, size: Int): List<SalesmanItem> = dbQuery {
        val salesmen = salesmanService.getBranchSalesmen(branchId, lastId, size)
        salesmen.map { it.toSalesmanItem() }
    }


    suspend fun getSalesmanById(salesmanId: Int): Result<ResponseSalesman> = dbQuery {
        salesmanService.getSalesmanById(salesmanId)?.let {
            Result.Success(it.toResponseSalesman())
        } ?: Result.Error(ApiException(SALESMAN_NOT_FOUND))
    }

//    suspend fun getSalesmanByNationalId(nationalId: String): Result<ResponseSalesman> = dbQuery {
//        salesmanRepository.getSalesmanByNationalId(nationalId)?.let {
//            Result.Success(it.toResponseSalesman())
//        } ?: Result.Error(SalesmanException("No salesman with this national id -> id = $nationalId"))
//    }
//
//    suspend fun getSalesmanByNEmail(nationalId: String): Result<ResponseSalesman> = dbQuery {
//        salesmanRepository.getSalesmanByNationalId(nationalId)?.let {
//            Result.Success(it.toResponseSalesman())
//        } ?: Result.Error(SalesmanException("No salesman with this national id -> id = $nationalId"))
//    }
//
//    suspend fun getSalesmanBySimNumber(number: String): Result<ResponseSalesman> = dbQuery {
//        salesmanRepository.getSalesmanBySimNumber(number)?.let {
//            Result.Success(it.toResponseSalesman())
//        } ?: Result.Error(SalesmanException("No salesman assigned to this sim number -> number = $number"))
//    }
//
//    suspend fun getSalesmanByIMEI(imei: Long): Result<ResponseSalesman> = dbQuery {
//        salesmanRepository.getSalesmanByIMEI(imei)?.let {
//            Result.Success(it.toResponseSalesman())
//        } ?: Result.Error(SalesmanException("No salesman assigned to this IMEI -> imei = $imei"))
//    }

    suspend fun updateSalesman(companyUID: String, salesmanId: Int, putSalesman: PutSalesman): Result<Boolean> =
        dbQuery {
            when {
                companyService.isCompanyEnabled(companyUID).not() -> Result.Error(ApiException(COMPANY_DISABLE))

                putSalesman.email?.let { isEmailUsed(it) } == true -> Result.Error(ApiException(EMAIL_ALREADY_TAKEN))

                putSalesman.nationalId?.let { isNationalIdUsed(it) } == true -> Result.Error(
                    ApiException(
                        NATIONAL_ID_ALREADY_TAKEN
                    )
                )

                putSalesman.assignedDeviceIMEI?.let { isIMEIUsed(it) } == true -> Result.Error(
                    ApiException(
                        IMEI_ALREADY_ASSIGNED
                    )
                )

                putSalesman.assignedSimNumber?.let { isSimNumberUsed(it) } == true -> Result.Error(
                    ApiException(
                        SIM_NUMBER_ALREADY_ASSIGNED
                    )
                )

                salesmanService.updateSalesman(salesmanId, putSalesman) -> Result.Success(true)
                else -> Result.Error(ApiException(SALESMAN_UPDATE_ERROR))
            }
        }

    suspend fun deleteSalesman(companyUID: String, salesmanId: Int): Result<Boolean> = dbQuery {
        if (salesmanService.deleteSalesman(companyUID, salesmanId)) {
            Result.Success(true)
        } else {
            Result.Error(
                ApiException(SALESMAN_DELETE_ERROR)
            )
        }
    }
}