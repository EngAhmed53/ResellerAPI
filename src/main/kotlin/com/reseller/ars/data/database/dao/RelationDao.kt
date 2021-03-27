package com.reseller.ars.data.database.dao

import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.Relation
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import java.lang.IllegalArgumentException

interface RelationDao {
    fun createNewRelation(relation: Relation)
    fun getIdsOfRelationType(type: EntityType): RelationDaoImpl.RelationFilter
    fun deleteRelationsOfType(companyUID: String, type: EntityType, vararg ids: Int)
}

object RelationDaoImpl : IntIdTable(), RelationDao {
    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updated_at = long("updated_at").default(System.currentTimeMillis())
    val entityType = enumerationByName("type", 50, EntityType::class)
    val companyId = varchar("company_id", 200)
    val branchId = integer("branch_id").nullable()
    val salesmanId = integer("salesman_id").nullable()
    val customerId = integer("customer_id").nullable()
    val invoiceId = integer("invoice_id").nullable()

    override fun createNewRelation(relation: Relation) {
        insert {
            it[entityType] = relation.type
            it[companyId] = relation.companyId
            it[branchId] = relation.branchId
            it[salesmanId] = relation.salesmanId
            it[customerId] = relation.customerId
            it[invoiceId] = relation.invoiceId
        }
    }

    override fun getIdsOfRelationType(type: EntityType): RelationFilter {
        return RelationFilter(type)
    }

    override fun deleteRelationsOfType(companyUID: String, type: EntityType, vararg ids: Int) {
        val idsList = ids.toList()
        deleteWhere {
            (companyId eq companyUID) and (entityType eq type) and (when (type) {
                EntityType.COMPANY -> throw IllegalArgumentException("$type is not allowed here")
                EntityType.BRANCH -> branchId inList idsList
                EntityType.SALESMAN -> salesmanId inList idsList
                EntityType.CUSTOMER -> customerId inList idsList
                EntityType.INVOICE -> invoiceId inList idsList
            })
        }
    }


    class RelationFilter(private val requiredType: EntityType) {

        fun filterBy(companyUID: String): List<Int> {
            return select {
                (companyId eq companyUID) and (entityType eq requiredType)
            }.mapNotNull {
                it.toIdOf(requiredType)
            }
        }

        fun filterBy(companyUID: String, filterType: EntityType, filterTypeId: Int): List<Int> {
            return select {
                (companyId eq companyUID) and (entityType eq requiredType) and (when (filterType) {
                    EntityType.COMPANY -> throw IllegalArgumentException("$filterType is not allowed here")
                    EntityType.BRANCH -> branchId eq filterTypeId
                    EntityType.SALESMAN -> salesmanId eq filterTypeId
                    EntityType.CUSTOMER -> customerId eq filterTypeId
                    EntityType.INVOICE -> invoiceId eq filterTypeId
                })
            }.mapNotNull {
                it.toIdOf(requiredType)
            }
        }

        private fun ResultRow.toIdOf(type: EntityType): Int? {
            return when (type) {
                EntityType.BRANCH -> this[branchId]
                EntityType.SALESMAN -> this[salesmanId]
                EntityType.CUSTOMER -> this[customerId]
                EntityType.INVOICE -> this[invoiceId]
                else -> null
            }
        }
    }
}

