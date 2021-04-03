package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.Branch
import com.reseller.ars.data.model.PutBranch

interface BranchRepository {
    fun createBranch(companyUID: String, branch: Branch): Int

    fun getBranchById(branchId: Int): Branch?

    fun getCompanyBranches(companyUID: String, lastId: Int, offset: Int): List<Branch>

    fun updateBranchInfo(branchId: Int, putBranch: PutBranch): Boolean

    fun deleteBranch(branchId: Int): Boolean
}