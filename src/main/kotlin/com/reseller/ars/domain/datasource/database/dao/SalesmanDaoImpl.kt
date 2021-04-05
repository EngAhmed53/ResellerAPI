package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.PutSalesman
import com.reseller.ars.data.model.ResponseSalesman
import com.reseller.ars.data.model.Salesman
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

object SalesmanDaoImpl : IntIdTable("salesmen"), SalesmanDao {

    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val uid = varchar("salesman_uid", 200).uniqueIndex()
    val firstName = varchar("first_name", 200)
    val lastName = varchar("last_name", 200)
    val email = varchar("email", 200).uniqueIndex()
    val nationalId = varchar("national_id", 50).uniqueIndex()
    val assignedSimNumber = varchar("sim_number", 50).uniqueIndex()
    val assignedDeviceIMEI = long("imei").uniqueIndex()
    val enabled = bool("enabled").default(true)

    override fun insert(obj: Salesman): Int {
        return insert {
            it[uid] = obj.uid
            it[firstName] = obj.firstName
            it[lastName] = obj.lastName
            it[email] = obj.email
            it[nationalId] = obj.nationalId
            it[assignedSimNumber] = obj.assignedSimNumber
            it[assignedDeviceIMEI] = obj.assignedDeviceIMEI
            it[enabled] = obj.enabled
        }[id].value
    }

    override fun selectByCompanyUID(companyUID: String, lastId: Int, size: Int): List<Salesman> {
        val complexJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = id, otherColumn = RelationDaoImpl.salesmanId,
            additionalConstraint = {
                (RelationDaoImpl.companyId eq companyUID) and
                        (RelationDaoImpl.entityType eq EntityType.SALESMAN)
            },
        )

        return complexJoin
            .slice(
                id,
                firstName, lastName, email, nationalId,
                assignedSimNumber, assignedDeviceIMEI,
                enabled
            ).select { (id greater lastId) }.limit(size)
            .orderBy(id to SortOrder.ASC).mapNotNull {
                it.toResponseSalesman()
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
                id,
                firstName, lastName, email, nationalId,
                assignedSimNumber, assignedDeviceIMEI,
                enabled
            ).select { (id greater lastId) }.limit(size)
            .orderBy(id to SortOrder.ASC).mapNotNull {
                it.toResponseSalesman()
            }
    }

    override fun selectById(id: Int): Salesman? {
        return select {
            (this@SalesmanDaoImpl.id eq id)
        }.mapNotNull {
            it.toResponseSalesman()
        }.singleOrNull()
    }

    override fun selectByEmail(email: String): Salesman? {
        return select {
            (this@SalesmanDaoImpl.email eq email)
        }.mapNotNull {
            it.toResponseSalesman()
        }.singleOrNull()
    }

    override fun selectByNationalId(nationalId: String): Salesman? {
        return select {
            (this@SalesmanDaoImpl.nationalId eq nationalId)
        }.mapNotNull {
            it.toResponseSalesman()
        }.singleOrNull()
    }

    override fun selectBySimNumber(simNumber: String): Salesman? {
        return select {
            (assignedSimNumber eq simNumber)
        }.mapNotNull {
            it.toResponseSalesman()
        }.singleOrNull()
    }

    override fun selectByIMEI(imei: Long): Salesman? {
        return select {
            (assignedDeviceIMEI eq imei)
        }.mapNotNull {
            it.toResponseSalesman()
        }.singleOrNull()
    }

    private fun ResultRow.toResponseSalesman(): Salesman =
        Salesman(
            id = this[id].value,
            uid = this[uid],
            firstName = this[firstName],
            lastName = this[lastName],
            email = this[email],
            nationalId = this[nationalId],
            assignedSimNumber = this[assignedSimNumber],
            assignedDeviceIMEI = this[assignedDeviceIMEI],
            enabled = this[enabled]
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
            putSalesman.nationalId?.let { salesman[nationalId] = it }
            putSalesman.assignedSimNumber?.let { salesman[assignedSimNumber] = it }
            putSalesman.assignedDeviceIMEI?.let { salesman[assignedDeviceIMEI] = it }
            putSalesman.enabled?.let { salesman[enabled] = it }
        } > 0
    }

    override fun delete(id: Int): Boolean {
        return deleteWhere { (this@SalesmanDaoImpl.id eq id) } > 0
    }
}