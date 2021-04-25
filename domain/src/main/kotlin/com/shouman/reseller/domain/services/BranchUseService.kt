package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.core.BaseUseCase
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repositories.BranchRepository
import com.shouman.reseller.domain.repositories.RelationRepository
import org.koin.core.inject


interface BranchUseService {
    fun createBranch(companyUID: String, branch: Branch): Int

    fun getBranchById(id: Int): Branch?

    fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): List<Branch>

    fun updateBranch(companyUID: String, branchId: Int, putBranch: PutBranch): Boolean

    fun deleteBranch(companyUID: String, branchId: Int): Boolean
}