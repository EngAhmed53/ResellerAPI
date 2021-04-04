package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.Branch
import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.PutBranch
import com.reseller.ars.data.model.ResponseBranch
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

interface BranchDao {
    fun insertBranch(companyUID: String, branch: Branch): Int

    fun selectBranchById(branchId: Int): ResponseBranch?

    fun selectBranchesByCompanyUID(companyUID: String, lastId: Int, size: Int): List<ResponseBranch>

    fun updateBranch(branchId: Int, putBranch: PutBranch): Boolean

    fun deleteBranch(branchId: Int): Boolean
}

object BranchDaoImpl : IntIdTable(), BranchDao {

    private val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val name = varchar("name", 200)
    val city = varchar("city", 100)
    val country = varchar("country", 100)

    override fun insertBranch(companyUID: String, branch: Branch): Int {
        return insert {
            it[name] = branch.name
            it[city] = branch.city
            it[country] = branch.country
        }[id].value
    }

    override fun selectBranchById(branchId: Int): ResponseBranch? {
        return select {
            (id eq branchId)
        }.mapNotNull {
            it.mapRowToResponseBranch()
        }.singleOrNull()
    }

    override fun selectBranchesByCompanyUID(companyUID: String, lastId: Int, size: Int): List<ResponseBranch> {
        val complexJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = id, otherColumn = RelationDaoImpl.branchId,
            additionalConstraint = {
                (RelationDaoImpl.companyId eq companyUID) and
                        (RelationDaoImpl.entityType eq EntityType.BRANCH)
            },
        )

        return complexJoin.slice(id, name, city, country).select { (id greater lastId) }.limit(size)
            .orderBy(id to SortOrder.ASC).mapNotNull {
            it.mapRowToResponseBranch()
        }
    }

    private fun ResultRow.mapRowToResponseBranch() =
        ResponseBranch(
            id = this[id].value,
            name = this[name],
            city = this[city],
            country = this[country]
        )

    override fun updateBranch(branchId: Int, putBranch: PutBranch): Boolean {
        return update({ id eq branchId }) { branch ->
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