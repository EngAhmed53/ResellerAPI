package com.shouman.reseller.data.repositoriesImpl

import com.shouman.reseller.data.datasource.database.dao.BranchDao
import com.shouman.reseller.domain.repository.BranchRepository
import com.shouman.reseller.domain.entities.Branch
import com.shouman.reseller.domain.entities.PutBranch
import org.koin.core.KoinComponent
import org.koin.core.inject

class BranchRepositoryImpl : BranchRepository, KoinComponent {

    private val branchDao by inject<BranchDao>()

    override fun createBranch(branch: Branch): Int {
        return branchDao.insert(branch)
    }

    override fun getBranchById(branchId: Int): Branch? {
        return branchDao.selectById(branchId)
    }

    override fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): List<Branch> {
       return branchDao.selectByCompanyUID(companyUID, lastId, size)
    }

    override fun updateBranchInfo(branchId: Int, putBranch: PutBranch): Boolean {
        return branchDao.update(branchId, putBranch)
    }

    override fun deleteBranch(branchId: Int): Boolean {
        return branchDao.delete(branchId)
    }
}