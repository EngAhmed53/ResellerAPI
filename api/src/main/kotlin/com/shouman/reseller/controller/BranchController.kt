package com.shouman.reseller.controller
import com.shouman.reseller.domain.core.mappers.toResponseBranch
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.entities.ResponseCode.*
import com.shouman.reseller.domain.services.*

class BranchController(
    private val companyService: CompanyService,
    private val branchUseService: BranchUseService
) : BaseController() {

    suspend fun creatCompanyBranch(companyUID: String, branch: Branch): Result<Int> = dbQuery {
        if (companyService.isCompanyEnabled(companyUID)) {
            val branchId = branchUseService.createBranch(companyUID, branch)
            Result.Success(branchId)
        } else {
            Result.Error(ApiException(COMPANY_DISABLED))
        }
    }

    suspend fun getBranchById(branchId: Int): Result<ResponseBranch> = dbQuery {
        branchUseService.getBranchById(branchId)?.let {
            Result.Success(it.toResponseBranch())
        } ?: Result.Error(ApiException(BRANCH_NOT_FOUND))
    }

    suspend fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): List<ResponseBranch> = dbQuery {
        val branches = branchUseService.getCompanyBranches(companyUID, lastId, size)
        branches.map { it.toResponseBranch() }
    }

    suspend fun updateBranchInfo(companyUID: String, branchId: Int, putBranch: PutBranch) = dbQuery {
        if (companyService.isCompanyEnabled(companyUID)) {
            when (branchUseService.updateBranch(companyUID, branchId, putBranch)) {
                true -> Result.Success(true)
                else -> Result.Error(ApiException(BRANCH_UPDATE_ERROR))
            }
        } else {
            Result.Error(ApiException(COMPANY_DISABLE))
        }
    }

    suspend fun deleteCompanyBranchById(companyUID: String, branchId: Int): Result<Boolean> = dbQuery {
        if (branchUseService.deleteBranch(companyUID, branchId)) {
            Result.Success(true)
        } else {
            Result.Error(
                ApiException(BRANCH_DELETE_ERROR)
            )
        }
    }
}