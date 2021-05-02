package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.Branch
import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.PutBranch
import com.shouman.reseller.domain.entities.Relation
import com.shouman.reseller.domain.repositories.BranchRepository
import com.shouman.reseller.domain.repositories.RelationRepository

internal class BranchUseServiceImpl(
    private val relationRepository: RelationRepository,
    private val branchRepository: BranchRepository
) : BranchUseService {

    override fun createBranch(companyUID: String, branch: Branch): Int {
        return branchRepository.createBranch(branch).also {
            relationRepository.createRelation(Relation(EntityType.BRANCH, companyUID, it))
        }
    }

    override fun getBranchById(id: Int): Branch? {
        return branchRepository.getBranchById(id)
    }

    override fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): List<Branch> {
        return branchRepository.getCompanyBranches(companyUID, lastId, size)
    }

    override fun updateBranch(companyUID: String, branchId: Int, putBranch: PutBranch): Boolean {
        return if (relationRepository.getRelation(companyUID, EntityType.BRANCH, branchId) != null) {
            branchRepository.updateBranchInfo(branchId, putBranch)
        } else {
            false
        }
    }

    override fun deleteBranch(companyUID: String, branchId: Int): Boolean {
        return relationRepository.deleteRelation(companyUID, EntityType.BRANCH, branchId).also {
            if (it) branchRepository.deleteBranch(branchId)
        }
    }

}