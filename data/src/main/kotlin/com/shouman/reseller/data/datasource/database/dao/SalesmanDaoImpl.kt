package com.shouman.reseller.data.datasource.database.dao

import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.PutSalesman
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.SalesmanBranch
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

object SalesmanDaoImpl : IntIdTable("salesmen"), SalesmanDao {

    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val uid = varchar("salesman_uid", 200).uniqueIndex()
    val firstName = varchar("first_name", 200)
    val lastName = varchar("last_name", 200)
    val email = varchar("email", 200).uniqueIndex()
    val assignedSimNumber = varchar("sim_number", 50).uniqueIndex()
    val assignedDeviceIMEI = long("imei").uniqueIndex()
    val enabled = bool("enabled").default(true)

    override fun insert(obj: Salesman): Int {
        return insert {
            it[uid] = obj.uid
            it[firstName] = obj.firstName
            it[lastName] = obj.lastName
            it[email] = obj.email
            it[assignedSimNumber] = obj.assignedSimNumber
            it[assignedDeviceIMEI] = obj.assignedDeviceIMEI
            it[enabled] = obj.enabled
        }[id].value
    }

    override fun selectByCompanyUID(companyUID: String, lastId: Int, size: Int): List<SalesmanBranch> {

        val relationSalesmenJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = id, otherColumn = RelationDaoImpl.salesmanId,
            additionalConstraint = {
                (RelationDaoImpl.companyId eq companyUID) and
                        (RelationDaoImpl.entityType eq EntityType.SALESMAN)
            },
        )

        val branchSalesmanRelationJoin = relationSalesmenJoin.join(
            otherTable = BranchDaoImpl,
            joinType = JoinType.INNER,
            onColumn = RelationDaoImpl.branchId,
            otherColumn = BranchDaoImpl.id
        )

        return branchSalesmanRelationJoin
            .slice(
                id,
                firstName, lastName,
                enabled,
                BranchDaoImpl.name
            ).select { (id greater lastId) }.limit(size)
            .orderBy(id to SortOrder.ASC).mapNotNull {
                it.toSalesmanBranch()
            }
    }

    override fun selectByBranchId(branchId: Int, lastId: Int, size: Int): List<Salesman> {
        val complexJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = id, otherColumn = RelationDaoImpl.salesmanId,
            additionalConstraint = {
                (RelationDaoImpl.branchId eq branchId) and
                        (RelationDaoImpl.entityType eq EntityType.SALESMAN)
            },
        )

        return complexJoin
            .slice(
                id, uid,
                firstName, lastName, email,
                assignedSimNumber, assignedDeviceIMEI,
                enabled
            ).select { (id greater lastId) }.limit(size)
            .orderBy(id to SortOrder.ASC).mapNotNull {
                it.toSalesman()
            }
    }

    override fun selectById(id: Int): Salesman? {
        return select {
            (this@SalesmanDaoImpl.id eq id)
        }.mapNotNull {
            it.toSalesman()
        }.singleOrNull()
    }

    override fun selectByEmail(email: String): Salesman? {
        return select {
            (SalesmanDaoImpl.email eq email)
        }.mapNotNull {
            it.toSalesman()
        }.singleOrNull()
    }

    override fun selectBySimNumber(simNumber: String): Salesman? {
        return select {
            (assignedSimNumber eq simNumber)
        }.mapNotNull {
            it.toSalesman()
        }.singleOrNull()
    }

    override fun selectByIMEI(imei: Long): Salesman? {
        return select {
            (assignedDeviceIMEI eq imei)
        }.mapNotNull {
            it.toSalesman()
        }.singleOrNull()
    }

    private fun ResultRow.toSalesman(): Salesman =
        Salesman(
            id = this[id].value,
            uid = this[uid],
            firstName = this[firstName],
            lastName = this[lastName],
            email = this[email],
            assignedSimNumber = this[assignedSimNumber],
            assignedDeviceIMEI = this[assignedDeviceIMEI],
            enabled = this[enabled]
        )

    private fun ResultRow.toSalesmanBranch(): SalesmanBranch =
        SalesmanBranch(
            id = this[id].value,
            firstName = this[firstName],
            lastName = this[lastName],
            enabled = this[enabled],
            branchName = this[BranchDaoImpl.name]
        )

    override fun isEnabled(salesmanId: Int): Boolean {
        val isEnabled: Boolean? = slice(enabled)
            .select {
                (id eq salesmanId)
            }.mapNotNull {
                it[enabled]
            }.singleOrNull()

        return isEnabled ?: false
    }

    override fun update(salesmanId: Int, putSalesman: PutSalesman): Boolean {
        return update({ id eq salesmanId }) { salesman ->
            salesman[updatedAt] = System.currentTimeMillis()
            putSalesman.firstName?.let { salesman[firstName] = it }
            putSalesman.lastName?.let { salesman[lastName] = it }
            putSalesman.email?.let { salesman[email] = it }
            putSalesman.assignedSimNumber?.let { salesman[assignedSimNumber] = it }
            putSalesman.assignedDeviceIMEI?.let { salesman[assignedDeviceIMEI] = it }
            putSalesman.enabled?.let { salesman[enabled] = it }
        } > 0
    }

    override fun delete(id: Int): Boolean {
        return deleteWhere { (this@SalesmanDaoImpl.id eq id) } > 0
    }
}