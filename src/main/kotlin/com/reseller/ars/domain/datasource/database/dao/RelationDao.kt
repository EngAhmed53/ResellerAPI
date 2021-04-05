package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.Relation
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.koin.core.KoinComponent
import java.lang.IllegalArgumentException

interface RelationDao {
    fun insert(relation: Relation): Int

    fun deleteByCompanyId(companyUID: String, type: EntityType, vararg ids: Int): Boolean
}

object RelationDaoImpl : IntIdTable(), RelationDao, KoinComponent {
    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val entityType = enumerationByName("type", 50, EntityType::class)
    val companyId = reference("company_id", CompanyDaoImpl.uid)
    val branchId = integer(name = "branch_id").references(BranchDaoImpl.id).nullable()
    val salesmanId = integer("salesman_id").nullable()
    val customerId = integer("customer_id").nullable()
    val invoiceId = integer("invoice_id").nullable()

    override fun insert(relation: Relation): Int {
        return insert {
            it[entityType] = relation.type
            it[companyId] = relation.companyId
            it[branchId] = relation.branchId
            it[salesmanId] = relation.salesmanId
            it[customerId] = relation.customerId
            it[invoiceId] = relation.invoiceId
        }[id].value
    }

    override fun deleteByCompanyId(companyUID: String, type: EntityType, vararg ids: Int): Boolean {
        val idsList = ids.toList()
        return deleteWhere {
            (companyId eq companyUID) and (entityType eq type) and (when (type) {
                EntityType.COMPANY -> throw IllegalArgumentException("$type is not allowed here")
                EntityType.BRANCH -> branchId inList idsList
                EntityType.SALESMAN -> salesmanId inList idsList
                EntityType.CUSTOMER -> customerId inList idsList
                EntityType.INVOICE -> invoiceId inList idsList
            })
        } > 0
    }
}

