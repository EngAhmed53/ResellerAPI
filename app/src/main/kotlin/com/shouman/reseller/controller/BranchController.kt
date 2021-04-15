package com.shouman.reseller.controller

import com.shouman.reseller.core.exception.BranchException
import com.shouman.reseller.core.exception.CompanyException
import com.shouman.reseller.domain.core.mappers.toResponseBranch
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repository.BranchRepository
import com.shouman.reseller.domain.repository.CompanyRepository
import com.shouman.reseller.domain.repository.RelationRepository
import com.shouman.reseller.domain.useCases.*
import org.koin.core.KoinComponent
import org.koin.core.inject


class BranchController(
    private val isCompanyEnabledUseCase: (String) -> Boolean = { IsCompanyEnabledUseCase(it).invoke() },
    private val createBranchUseCase: (Branch, String) -> Int = { branch, companyUID ->
        CreateBranchUseCase(branch, companyUID).invoke()
    },
    private val getCompanyBranchesUseCase: (uid: String, lastId: Int, size: Int) -> List<Branch> = { uid, lastId, size ->
        GetCompanyBranchesUseCase(uid, lastId, size).invoke()
    },
    private val getBranchByIdUseCase: (Int) -> Branch? = { GetBranchByIdUSeCase(it).invoke() },
    private val updateBranchInfoUseCase: (branchId: Int, putBranch: PutBranch) -> Boolean = { id, putBranch ->
        UpdateBranchInfoUseCase(id, putBranch).invoke()
    },
    private val deleteBranchUseCase: (companyUID: String, branchID: Int) -> Boolean = { companyUID, branchID ->
        DeleteBranchUseCase(companyUID, branchID).invoke()
    }
) : BaseController() {

    suspend fun creatCompanyBranch(companyUID: String, branch: Branch): Result<Int> = dbQuery {
        if (isCompanyEnabledUseCase.invoke(companyUID)) {
            val branchId = createBranchUseCase.invoke(branch, companyUID)
            Result.Success(branchId)
        } else {
            Result.Error(CompanyException("Company is disabled"))
        }
    }

    suspend fun getBranchById(branchId: Int): Result<ResponseBranch> = dbQuery {
        getBranchByIdUseCase.invoke(branchId)?.let {
            Result.Success(it.toResponseBranch())
        } ?: Result.Error(BranchException("No branch with this id -> id = $branchId"))
    }

    suspend fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): List<ResponseBranch> = dbQuery {
        val branches = getCompanyBranchesUseCase.invoke(companyUID, lastId, size)
        branches.map { it.toResponseBranch() }
    }

    suspend fun updateBranchInfo(companyUID: String, branchId: Int, putBranch: PutBranch) = dbQuery {
        if (isCompanyEnabledUseCase.invoke(companyUID)) {
            when (updateBranchInfoUseCase.invoke(branchId, putBranch)) {
                true -> Result.Success(true)
                else -> Result.Error(BranchException("Error updating branch $branchId in company $companyUID"))
            }
        } else {
            Result.Error(CompanyException("Company is disabled"))
        }
    }

    suspend fun deleteCompanyBranchById(companyUID: String, branchId: Int): Result<Boolean> = dbQuery {
        if (deleteBranchUseCase.invoke(companyUID, branchId)) {
            Result.Success(true)
        } else {
            Result.Error(
                BranchException("Branch $branchId in company $companyUID deletion failed, might be deleted already")
            )
        }
    }
}