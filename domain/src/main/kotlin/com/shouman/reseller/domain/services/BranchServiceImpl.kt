package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repositories.BranchRepository

internal class BranchServiceImpl(
    private val companyService: CompanyService,
    private val relationService: RelationService,
    private val branchRepository: BranchRepository
) : BranchService {

    override fun createBranch(companyUid: String, branch: Branch): Int {

        if (companyService.isCompanyEnabled(companyUid).not()) {
            return -1
        }

        return branchRepository.createBranch(branch).also {
            relationService.addRelation(Relation(EntityType.BRANCH, companyUid, it))
        }
    }

    override fun getBranchById(id: Int): Branch? {
        return branchRepository.getBranchById(id)
    }

    override fun getCompanyBranches(companyUID: String, lastId: Int, size: Int): Pair<StatusCode, List<Branch>> {

        if (relationService.isValidRelation(Relation(EntityType.COMPANY, companyId = companyUID)).not()) {
            return StatusCode.INVALID_RELATION to emptyList()
        }

        val branchesList = branchRepository.getCompanyBranches(companyUID, lastId, size)

        return StatusCode.SUCCESS to branchesList
    }

    override fun updateBranch(companyUID: String, branchId: Int, putBranch: PutBranch): StatusCode {
        if (relationService.isValidRelation(Relation(EntityType.BRANCH, companyUID, branchId)).not()) {
            return StatusCode.INVALID_RELATION
        }

        val isUpdated = branchRepository.updateBranchInfo(branchId, putBranch)

        return if (isUpdated) {
            StatusCode.SUCCESS
        } else {
            StatusCode.UPDATE_ERROR
        }
    }

    override fun deleteBranch(companyUID: String, branchId: Int): StatusCode {

        if (relationService.isValidRelation(Relation(EntityType.BRANCH, companyUID, branchId)).not()) {
            return StatusCode.INVALID_RELATION
        }

        val isDeleted =
            branchRepository.takeIf { relationService.deleteRelation(companyUID, EntityType.BRANCH, branchId) }
                ?.deleteBranch(branchId) ?: return StatusCode.DELETE_ERROR

        return if (isDeleted) StatusCode.SUCCESS
        else StatusCode.DELETE_ERROR
    }

}