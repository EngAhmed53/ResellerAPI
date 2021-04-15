package com.shouman.reseller.domain.useCases

import com.shouman.reseller.domain.core.BaseUseCase
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repository.BranchRepository
import com.shouman.reseller.domain.repository.RelationRepository
import org.koin.core.inject


class CreateBranchUseCase(
    private val branch: Branch,
    private val companyUID: String
) : BaseUseCase<Int> {

    private val branchRepository by inject<BranchRepository>()
    private val relationRepository by inject<RelationRepository>()

    override fun invoke(): Int {
        return branchRepository.createBranch(branch).also {
            relationRepository.createRelation(Relation(EntityType.BRANCH, companyUID, it))
        }
    }
}

class GetBranchByIdUSeCase(
    private val id: Int
) : BaseUseCase<Branch?> {
    private val branchRepository by inject<BranchRepository>()

    override fun invoke(): Branch? {
        return branchRepository.getBranchById(id)
    }
}

class GetCompanyBranchesUseCase(
    private val companyUID: String,
    private val lastId: Int,
    private val size: Int
) : BaseUseCase<List<Branch>> {

    private val branchRepository by inject<BranchRepository>()

    override fun invoke(): List<Branch> {
        return branchRepository.getCompanyBranches(companyUID, lastId, size)
    }
}

class UpdateBranchInfoUseCase(
    private val branchId: Int,
    private val putBranch: PutBranch
): BaseUseCase<Boolean> {

    private val branchRepository by inject<BranchRepository>()

    override fun invoke(): Boolean {
        return branchRepository.updateBranchInfo(branchId, putBranch)
    }
}

class DeleteBranchUseCase(
    private val companyUID: String,
    private val branchId: Int
): BaseUseCase<Boolean> {

    private val branchRepository by inject<BranchRepository>()
    private val relationRepository by inject<RelationRepository>()

    override fun invoke(): Boolean {
        return branchRepository.deleteBranch(branchId).also {
            if (it) relationRepository.deleteRelation(companyUID, EntityType.BRANCH, branchId)
        }
    }
}