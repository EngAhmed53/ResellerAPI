package com.shouman.reseller.domain.repository

import com.shouman.reseller.domain.entities.Branch
import com.shouman.reseller.domain.entities.PutBranch


interface BranchRepository {
    fun createBranch(branch: Branch): Int

    fun getBranchById(branchId: Int): Branch?

    fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): List<Branch>

    fun updateBranchInfo(branchId: Int, putBranch: PutBranch): Boolean

    fun deleteBranch(branchId: Int): Boolean
}