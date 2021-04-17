package com.shouman.reseller.controller

import com.shouman.reseller.core.exception.CompanyException
import com.shouman.reseller.core.exception.SalesmanException
import com.shouman.reseller.domain.core.extensions.isNull
import com.shouman.reseller.domain.core.mappers.toResponseSalesman
import com.shouman.reseller.domain.core.mappers.toSalesmanItem

import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.useCases.*
import org.koin.core.KoinComponent


class SalesmanController(
    private val isCompanyEnabledUseCase: (String) -> Boolean = { IsCompanyEnabledUseCase(it).invoke() },
    private val getBranchByIdUseCase: (Int) -> Branch? = { GetBranchByIdUSeCase(it).invoke() },
    private val createSalesmanUseCase: (String, Int, Salesman) -> Int = { companyUID, branchId, salesman ->
        CreateSalesmanUseCase(companyUID, branchId, salesman).invoke()
    },
    private val isEmailUsed: (String) -> Boolean = { GetSalesmanByEmailUseCase(it).invoke() != null },
    private val isSimNumberUsed: (String) -> Boolean = { GetSalesmanBySimNumberUseCase(it).invoke() != null },
    private val isNationalIdUsed: (String) -> Boolean = { GetSalesmanByNationalIdUseCase(it).invoke() != null },
    private val isIMEIUsed: (Long) -> Boolean = { GetSalesmanByIMEIUseCase(it).invoke() != null },
    private val getSalesmanByIdUseCase: (Int) -> Salesman? = { GetSalesmanByIdUseCase(it).invoke() },
    private val getBranchSalesmenUseCase: (branchId: Int, lastId: Int, size: Int) -> List<Salesman> = { branchId, lastId, size ->
        GetBranchSalesmenUseCase(branchId, lastId, size).invoke()
    },
    private val getCompanySalesmenUseCase: (companyUID: String, lastId: Int, size: Int) -> List<SalesmanBranch> = { uid, lastId, size ->
        GetCompanySalesmenBranchUseCase(uid, lastId, size).invoke()
    },
    private val updateSalesmanInfoUseCase: (Int, PutSalesman) -> Boolean = { salesmanId, putSalesman ->
        UpdateSalesmanInfoUseCase(salesmanId, putSalesman).invoke()
    },
    private val deleteSalesmanUseCase: (String, Int) -> Boolean = { uid, salesmanId ->
        DeleteSalesmanUseCase(companyUID = uid, salesmanId = salesmanId).invoke()
    },
) : BaseController(), KoinComponent {

    suspend fun addSalesman(companyUID: String, branchId: Int, salesman: Salesman): Result<Int> =
        dbQuery {

            when {
                isCompanyEnabledUseCase(companyUID).not() -> Result.Error(CompanyException("Company is disabled"))

                getBranchByIdUseCase(branchId).isNull() -> Result.Error(SalesmanException("Branch id is wrong"))

                isEmailUsed(salesman.email) -> Result.Error(SalesmanException("email already used"))

                isNationalIdUsed(salesman.nationalId) -> Result.Error(SalesmanException("nationalId already used"))

                isIMEIUsed(salesman.assignedDeviceIMEI) -> Result.Error(SalesmanException("imei already used"))

                isSimNumberUsed(salesman.assignedSimNumber) -> Result.Error(SalesmanException("sim number already used"))

                else -> {
                    val id = createSalesmanUseCase(companyUID, branchId, salesman)
                    Result.Success(id)
                }
            }
        }


    suspend fun getCompanySalesmen(companyUID: String, lastId: Int, size: Int): List<SalesmanBranch> = dbQuery {
        getCompanySalesmenUseCase.invoke(companyUID, lastId, size)
    }


    suspend fun getBranchSalesmen(branchId: Int, lastId: Int, size: Int): List<SalesmanItem> = dbQuery {
        val salesmen = getBranchSalesmenUseCase.invoke(branchId, lastId, size)
        salesmen.map { it.toSalesmanItem() }
    }


    suspend fun getSalesmanById(salesmanId: Int): Result<ResponseSalesman> = dbQuery {
        getSalesmanByIdUseCase(salesmanId)?.let {
            Result.Success(it.toResponseSalesman())
        } ?: Result.Error(SalesmanException("No salesman with this id -> id = $salesmanId"))
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
                isCompanyEnabledUseCase(companyUID).not() -> Result.Error(CompanyException("Company is disabled"))

                putSalesman.email?.let { isEmailUsed(it) } == true -> Result.Error(SalesmanException("email already used"))

                putSalesman.nationalId?.let { isNationalIdUsed(it) } == true -> Result.Error(SalesmanException("nationalId already used"))

                putSalesman.assignedDeviceIMEI?.let { isIMEIUsed(it) } == true -> Result.Error(SalesmanException("imei already used"))

                putSalesman.assignedSimNumber?.let { isSimNumberUsed(it) } == true -> Result.Error(SalesmanException("sim number already used"))

                updateSalesmanInfoUseCase(salesmanId, putSalesman) -> Result.Success(true)
                else -> Result.Error(SalesmanException("Error updating salesman $salesmanId in company $companyUID"))
            }
        }

    suspend fun deleteSalesman(companyUID: String, salesmanId: Int): Result<Boolean> = dbQuery {
        if (deleteSalesmanUseCase.invoke(companyUID, salesmanId)) {
            Result.Success(true)
        } else {
            Result.Error(
                SalesmanException("Salesman $salesmanId in company $companyUID deletion failed, might be deleted already")
            )
        }
    }
}