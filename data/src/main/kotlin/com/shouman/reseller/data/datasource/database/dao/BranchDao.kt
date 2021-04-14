package com.shouman.reseller.data.datasource.database.dao

import com.shouman.reseller.domain.entities.Branch
import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.PutBranch
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

interface BranchDao: BaseDao<Int, Branch> {

    fun selectByCompanyUID(companyUID: String, lastId: Int, size: Int): List<Branch>

    fun update(branchId: Int, putBranch: PutBranch): Boolean
}

object BranchDaoImpl : IntIdTable(), BranchDao {

    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val name = varchar("name", 200)
    val city = varchar("city", 100)
    val country = varchar("country", 100)

    override fun insert(obj: Branch): Int {
        return insert {
            it[name] = obj.name
            it[city] = obj.city
            it[country] = obj.country
        }[id].value
    }

    override fun selectById(id: Int): Branch? {
        return select {
            (this@BranchDaoImpl.id eq id)
        }.mapNotNull {
            it.mapRowToResponseBranch()
        }.singleOrNull()
    }

    override fun selectByCompanyUID(companyUID: String, lastId: Int, size: Int): List<Branch> {
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
        Branch(
            id = this[id].value,
            name = this[name],
            city = this[city],
            country = this[country]
        )

    override fun update(branchId: Int, putBranch: PutBranch): Boolean {
        return update({ id eq branchId }) { branch ->
            branch[updatedAt] = System.currentTimeMillis()
            putBranch.name?.let { branch[name] = it }
            putBranch.city?.let { branch[city] = it }
            putBranch.country?.let { branch[country] = it }
        } > 0
    }

    override fun delete(id: Int): Boolean {
        return deleteWhere { (this@BranchDaoImpl.id eq id) } > 0
    }
}