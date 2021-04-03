package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.Branch
import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.PutBranch
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import java.util.*

interface BranchDao {
    fun insertBranch(companyUID: String, branch: Branch): String

    fun selectBranchById(branchId: Int): Branch?

    fun selectBranchesByCompanyUID(companyUID: String): List<Branch>

    fun updateBranch(putBranch: PutBranch): Boolean

    fun deleteBranch(branchId: Int): Boolean
}

object BranchDaoImpl : IntIdTable(), BranchDao {

    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val name = varchar("name", 200)
    val city = varchar("city", 100)
    val country = varchar("country", 100)

    override fun insertBranch(companyUID: String, branch: Branch): String {
        return insert {
            it[name] = branch.name
            it[city] = branch.city
            it[country] = branch.country
        }[id].value.toString()

    }

    override fun selectBranchById(branchId: Int): Branch? {
        return select {
            (id eq branchId)
        }.mapNotNull {
            it.mapRowToBranch()
        }.singleOrNull()
    }

    override fun selectBranchesByCompanyUID(companyUID: String): List<Branch> {
        val complexJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = id, otherColumn = RelationDaoImpl.branchId,
            additionalConstraint = {
                (RelationDaoImpl.companyId eq companyUID) and
                        (RelationDaoImpl.entityType eq EntityType.BRANCH)
            },
        )

        return complexJoin.slice(name, city, country).selectAll().mapNotNull {
            it.mapRowToBranch()
        }
    }

    private fun ResultRow.mapRowToBranch() =
        Branch(
            name = this[name],
            city = this[city],
            country = this[country]
        )

    override fun updateBranch(putBranch: PutBranch): Boolean {
        return update({ id eq putBranch.branchId }) { branch ->
            branch[updatedAt] = System.currentTimeMillis()
            putBranch.name?.let { branch[name] = it }
            putBranch.city?.let { branch[city] = it }
            putBranch.country?.let { branch[country] = it }
        } > 0
    }

    override fun deleteBranch(branchId: Int): Boolean {
        return deleteWhere { (id eq branchId) } > 0
    }
}