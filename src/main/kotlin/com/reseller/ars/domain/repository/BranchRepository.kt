package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.Branch
import com.reseller.ars.data.model.PutBranch
import com.reseller.ars.data.model.ResponseBranch

interface BranchRepository {
    fun createBranch(branch: Branch): Int

    fun getBranchById(branchId: Int): ResponseBranch?

    fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): List<ResponseBranch>

    fun updateBranchInfo(branchId: Int, putBranch: PutBranch): Boolean

    fun deleteBranch(branchId: Int): Boolean
}