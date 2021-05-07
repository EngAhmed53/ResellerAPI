package com.shouman.reseller.data.datasource.database.dao

import com.shouman.reseller.domain.entities.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

object CustomerDaoImpl : IntIdTable("customers"), CustomerDao {

    val createdAt = long("created_at")
    val updatedAt = long("updated_at")
    val companyUID = varchar("company_id", 200)
    val firstName = varchar("first_name", 200)
    val lastName = varchar("last_name", 200)
    val email = varchar("email", 200)
    val phoneNumber = varchar("phone_number", 50)

    override fun insert(companyUID: String, customer: Customer): Int {
        return insert {
            it[this@CustomerDaoImpl.companyUID] = companyUID
            it[createdAt] = customer.createdAt
            it[updatedAt] = customer.createdAt
            it[firstName] = customer.firstName
            it[lastName] = customer.lastName
            it[email] = customer.email
            it[phoneNumber] = customer.phoneNumber
        }[id].value
    }

    override fun selectById(companyUID: String, customerId: Int): Customer? {
        return select {
            (this@CustomerDaoImpl.id eq id) and (this@CustomerDaoImpl.companyUID eq companyUID)
        }.mapNotNull {
            it.mapRowToCustomer()
        }.singleOrNull()
    }

    override fun selectByEmail(companyUID: String, email: String): Customer? {
        return select {
            (this@CustomerDaoImpl.email eq email) and (this@CustomerDaoImpl.companyUID eq companyUID)
        }.mapNotNull {
            it.mapRowToCustomer()
        }.singleOrNull()
    }

    override fun selectByPhoneNumber(companyUID: String, number: String): Customer? {
        return select {
            (this@CustomerDaoImpl.phoneNumber eq number) and (this@CustomerDaoImpl.companyUID eq companyUID)
        }.mapNotNull {
            it.mapRowToCustomer()
        }.singleOrNull()
    }

    private fun ResultRow.mapRowToCustomer() =
        Customer(
            id = this[id].value,
            createdAt = this[createdAt],
            firstName = this[firstName],
            lastName = this[lastName],
            email = this[email],
            phoneNumber = this[phoneNumber]
        )

    override fun delete(companyUID: String, customerId: Int): Boolean {
        return deleteWhere { (this@CustomerDaoImpl.id eq customerId) and (this@CustomerDaoImpl.companyUID eq companyUID) } > 0
    }

    override fun selectByCompanyUID(uid: String, lastId: Int, size: Int): List<CompanyCustomer> {
        val relationCustomersJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = id, otherColumn = RelationDaoImpl.customerId,
            additionalConstraint = {
                (RelationDaoImpl.companyId eq uid) and
                        (RelationDaoImpl.entityType eq EntityType.CUSTOMER)
            },
        )

        val branchesCustomersRelationsJoin = relationCustomersJoin.join(
            otherTable = BranchDaoImpl,
            joinType = JoinType.INNER,
            onColumn = RelationDaoImpl.branchId,
            otherColumn = BranchDaoImpl.id
        )

        val salesmenBranchesCustomersRelationsJoin = branchesCustomersRelationsJoin.join(
            otherTable = SalesmanDaoImpl,
            joinType = JoinType.INNER,
            onColumn = RelationDaoImpl.salesmanId,
            otherColumn = SalesmanDaoImpl.id
        )

        return salesmenBranchesCustomersRelationsJoin
            .slice(
                id,
                firstName, lastName,
                SalesmanDaoImpl.firstName, SalesmanDaoImpl.lastName,
                BranchDaoImpl.name
            ).select { (id greater lastId) }.limit(size)
            .orderBy(id to SortOrder.ASC).mapNotNull {
                it.mapRowToCompanyCustomer()
            }
    }

    private fun ResultRow.mapRowToCompanyCustomer(): CompanyCustomer =
        CompanyCustomer(
            id = this[id].value,
            firstName = this[firstName],
            lastName = this[lastName],
            branchName = this[BranchDaoImpl.name],
            salesmanFirstName = this[SalesmanDaoImpl.firstName],
            salesmanLastName = this[SalesmanDaoImpl.lastName]
        )

    override fun selectByBranchId(id: Int, lastId: Int, size: Int): List<BranchCustomer> {
        val relationCustomersJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = CustomerDaoImpl.id, otherColumn = RelationDaoImpl.customerId,
            additionalConstraint = {
                (RelationDaoImpl.branchId eq id) and
                        (RelationDaoImpl.entityType eq EntityType.CUSTOMER)
            }
        )

        val salesmenCustomersRelationsJoin = relationCustomersJoin.join(
            otherTable = SalesmanDaoImpl,
            joinType = JoinType.INNER,
            onColumn = RelationDaoImpl.salesmanId,
            otherColumn = SalesmanDaoImpl.id
        )

        return salesmenCustomersRelationsJoin
            .slice(
                CustomerDaoImpl.id,
                firstName, lastName,
                SalesmanDaoImpl.firstName, SalesmanDaoImpl.lastName,
            ).select { (CustomerDaoImpl.id greater lastId) }.limit(size)
            .orderBy(CustomerDaoImpl.id to SortOrder.ASC).mapNotNull {
                it.mapRowToBranchCustomer()
            }
    }

    private fun ResultRow.mapRowToBranchCustomer(): BranchCustomer =
        BranchCustomer(
            id = this[id].value,
            firstName = this[firstName],
            lastName = this[lastName],
            salesmanFirstName = this[SalesmanDaoImpl.firstName],
            salesmanLastName = this[SalesmanDaoImpl.lastName]
        )

    override fun selectBySalesmanId(id: Int, lastId: Int, size: Int): List<SalesmanCustomer> {
        val relationCustomersJoin = Join(
            this, otherTable = RelationDaoImpl,
            onColumn = CustomerDaoImpl.id, otherColumn = RelationDaoImpl.customerId,
            additionalConstraint = {
                (RelationDaoImpl.salesmanId eq id) and
                        (RelationDaoImpl.entityType eq EntityType.CUSTOMER)
            }
        )

        return relationCustomersJoin
            .slice(
                CustomerDaoImpl.id,
                firstName, lastName,
            ).select { (CustomerDaoImpl.id greater lastId) }.limit(size)
            .orderBy(CustomerDaoImpl.id to SortOrder.ASC).mapNotNull {
                it.mapRowToSalesmanCustomer()
            }
    }

    private fun ResultRow.mapRowToSalesmanCustomer(): SalesmanCustomer =
        SalesmanCustomer(
            id = this[id].value,
            firstName = this[firstName],
            lastName = this[lastName]
        )

    override fun update(companyUID: String, customerId: Int, putCustomer: PutCustomer): Boolean {
        return update({ (id eq customerId) and (this@CustomerDaoImpl.companyUID eq companyUID)}) { customer ->
            customer[updatedAt] = putCustomer.updatedAt
            putCustomer.firstName?.let { customer[firstName] = it }
            putCustomer.lastName?.let { customer[lastName] = it }
            putCustomer.email?.let { customer[email] = it }
            putCustomer.phoneNumber?.let { customer[phoneNumber] = it }
        } > 0
    }
}