package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.Branch
import com.reseller.ars.data.model.PutBranch
import com.reseller.ars.domain.datasource.database.dao.CompanyDaoImpl.default
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

interface BranchDao {
    fun insertBranch(companyUID: String): Int

    fun getBranchById(branchId: Int): Branch

    fun getBranches(companyUID: String): List<Branch>

    fun editBranchInfo(putBranch: PutBranch): Boolean

    fun deleteBranch(branchId: Int): Boolean
}

class BranchDaoImpl : UUIDTable(), BranchDao {

    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val name = varchar("name", 200)
    val city = varchar("city", 100)
    val country = varchar("country", 100)

    override fun insertBranch(companyUID: String): Int {
        TODO("Not yet implemented")
    }

    override fun getBranchById(branchId: Int): Branch {
        TODO("Not yet implemented")
    }

    override fun getBranches(companyUID: String): List<Branch> {
        TODO("Not yet implemented")
    }

    override fun editBranchInfo(putBranch: PutBranch): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteBranch(branchId: Int): Boolean {
        TODO("Not yet implemented")
    }
}