package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.Branch
import com.reseller.ars.data.model.PutBranch
import com.reseller.ars.domain.datasource.database.dao.BranchDao
import org.koin.core.KoinComponent
import org.koin.core.inject

class BranchRepositoryImpl : BranchRepository, KoinComponent {

    private val branchDao by inject<BranchDao>()

    override fun createBranch(companyUID: String, branch: Branch): Int {
        return branchDao.insertBranch(companyUID, branch)
    }

    override fun getBranchById(branchId: Int): Branch? {
        return branchDao.selectBranchById(branchId)
    }

    override fun getCompanyBranches(companyUID: String, lastId: Int, offset: Int): List<Branch> {
       return branchDao.selectBranchesByCompanyUID(companyUID, lastId, offset)
    }

    override fun updateBranchInfo(branchId: Int, putBranch: PutBranch): Boolean {
        return branchDao.updateBranch(branchId, putBranch)
    }

    override fun deleteBranch(branchId: Int): Boolean {
        return branchDao.deleteBranch(branchId)
    }
}