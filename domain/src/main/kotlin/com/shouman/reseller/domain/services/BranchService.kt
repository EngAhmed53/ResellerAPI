package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*


interface BranchService {
    fun createBranch(companyUid: String, branch: Branch): Int

    fun getBranchById(id: Int): Branch?

    fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): Pair<StatusCode, List<Branch>>

    fun updateBranch(companyUID: String, branchId: Int, putBranch: PutBranch): StatusCode

    fun deleteBranch(companyUID: String, branchId: Int): StatusCode
}