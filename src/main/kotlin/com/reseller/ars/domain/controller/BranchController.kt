package com.reseller.ars.domain.controller

import com.reseller.ars.app.core.exception.BranchException
import com.reseller.ars.app.core.exception.CompanyException
import com.reseller.ars.data.model.*
import com.reseller.ars.domain.repository.BranchRepository
import com.reseller.ars.domain.repository.CompanyRepository
import com.reseller.ars.domain.repository.RelationRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class BranchController : BaseController(), KoinComponent {

    private val relationRepository by inject<RelationRepository>()
    private val branchRepository by inject<BranchRepository>()
    private val companyRepository by inject<CompanyRepository>()

    suspend fun creatCompanyBranch(companyUID: String, branch: Branch): Result<Int> = dbQuery {
        if (companyRepository.isCompanyEnabled(companyUID)) {
            val branchId = branchRepository.createBranch(branch).also {
                relationRepository.createRelation(Relation(EntityType.BRANCH, companyUID, it))
            }
            Result.Success(branchId)
        } else {
            Result.Error(CompanyException("Company is disabled"))
        }
    }

    suspend fun getBranchById(branchId: Int): Result<ResponseBranch> = dbQuery {
        branchRepository.getBranchById(branchId)?.let {
            Result.Success(it)
        } ?: Result.Error(BranchException("No branch with this id -> id = $branchId"))
    }

    suspend fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): Result<List<ResponseBranch>> = dbQuery {
        val branches = branchRepository.getCompanyBranches(companyUID, lastId, size)
        Result.Success(branches)
    }

    suspend fun updateBranchInfo(companyUID: String, branchId: Int, putBranch: PutBranch) = dbQuery {
        if (companyRepository.isCompanyEnabled(companyUID)) {
            when (branchRepository.updateBranchInfo(branchId, putBranch)) {
                true -> Result.Success(true)
                else -> Result.Error(BranchException("Error updating branch $branchId in company $companyUID"))
            }
        } else {
            Result.Error(CompanyException("Company is disabled"))
        }
    }

    suspend fun deleteCompanyBranchById(companyUID: String, branchId: Int): Result<Boolean> = dbQuery {
        if (branchRepository.deleteBranch(branchId)) {
            relationRepository.deleteRelation(companyUID, EntityType.BRANCH, branchId)
            Result.Success(true)
        } else {
            Result.Error(
                BranchException("Branch $branchId in company $companyUID deletion failed, might be deleted already")
            )
        }
    }
}