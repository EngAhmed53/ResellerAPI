package com.shouman.reseller.controller

import com.google.firebase.FirebaseException
import com.shouman.reseller.domain.core.extensions.isNull
import com.shouman.reseller.domain.core.mappers.toResponseSalesman
import com.shouman.reseller.domain.core.mappers.toSalesman
import com.shouman.reseller.domain.core.mappers.toSalesmanItem
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.entities.ResponseCode.*
import com.shouman.reseller.domain.services.*
import org.koin.core.KoinComponent

class SalesmanController(
    private val companyService: CompanyService,
    private val salesmanService: SalesmanService,
    private val branchUseService: BranchUseService,
    private val handheldService: HandheldService,
    private val firebaseAuthService: FirebaseAuthService
) : BaseController(), KoinComponent {

    private val isEmailUsed: (String) -> Boolean = { salesmanService.getSalesmanByEmail(it) != null }
    private val isSimNumberUsed: (String) -> Boolean = { salesmanService.getSalesmanBySimNumber(it) != null }
    private val isIMEIUsed: (Long) -> Boolean = { salesmanService.getSalesmanByIMEI(it) != null }

    suspend fun addSalesman(companyUID: String, branchId: Int, postSalesman: PostSalesman): Result<Int> =
        dbQuery {
            when {
                companyService.isCompanyEnabled(companyUID).not() -> Result.Error(ApiException(COMPANY_DISABLED))
                branchUseService.getBranchById(branchId).isNull() -> Result.Error(ApiException(BRANCH_NOT_FOUND))
                else -> {
                    var uid: String? = null
                    try {
                        val credential = firebaseAuthService.createSalesmanFirebaseAccount(postSalesman)
                        uid = credential.uid
                        val salesman = postSalesman.toSalesman(credential.uid)
                        val id = salesmanService.createSalesman(companyUID, branchId, salesman)
                        Result.Success(id)
                    } catch (e: Exception) {
                        uid?.let { firebaseAuthService.deleteSalesmanFirebaseAccount(it) }

                        when(e) {
                            is FirebaseException -> Result.Error(ApiException(F_AUTH_ERROR, e.message))
                            else -> Result.Error(ApiException(SALESMAN_CREATE_ERROR))
                        }
                    }
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


    suspend fun isEmailExist(email: String): Result<Boolean> = dbQuery {
        when (isEmailUsed.invoke(email)) {
            true -> Result.Error(ApiException(EMAIL_ALREADY_TAKEN))
            false -> Result.Success(true)
        }
    }

    suspend fun isSimNumberExist(number: String): Result<Boolean> = dbQuery {
        when (isSimNumberUsed.invoke(number)) {
            true -> Result.Error(ApiException(SIM_NUMBER_ALREADY_ASSIGNED))
            false -> Result.Success(true)
        }
    }

    suspend fun isIMEIAccepted(imei: Long): Result<Boolean> = dbQuery {
        when (isIMEIUsed.invoke(imei)) {
            true -> Result.Error(ApiException(IMEI_ALREADY_ASSIGNED)) // IMEI is already used, so it is not accepted
            false -> {
                when (handheldService.isHandheldModelAccepted(imei)) {
                    true -> Result.Success(true)
                    false -> Result.Error(ApiException(DEVICE_NOT_SUPPORTED))
                }
            }
        }
    }

    suspend fun updateSalesman(companyUID: String, salesmanId: Int, putSalesman: PutSalesman): Result<Boolean> =
        dbQuery {
            when {
                companyService.isCompanyEnabled(companyUID).not() -> Result.Error(ApiException(COMPANY_DISABLED))

                putSalesman.email?.let { isEmailUsed(it) } == true -> Result.Error(ApiException(EMAIL_ALREADY_TAKEN))

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