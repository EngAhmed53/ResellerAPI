package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.Relation
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.koin.core.KoinComponent
import java.lang.IllegalArgumentException

interface RelationDao {
    fun createNewRelation(relation: Relation): Int

    //fun getIdsOfRelationType(type: EntityType): RelationDaoImpl.RelationFilter

    fun deleteRelationsTypeByCompanyId(companyUID: String, type: EntityType, vararg ids: String): Boolean
}

object RelationDaoImpl : IntIdTable(), RelationDao, KoinComponent {
    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val entityType = enumerationByName("type", 50, EntityType::class)
    val companyId = varchar("company_id", 200)
    val branchId = varchar("branch_id", 200).nullable()
    val salesmanId = varchar("salesman_id", 200).nullable()
    val customerId = varchar("customer_id", 200).nullable()
    val invoiceId = varchar("invoice_id", 200).nullable()

    override fun createNewRelation(relation: Relation): Int {
        return insert {
            it[entityType] = relation.type
            it[companyId] = relation.companyId
            it[branchId] = relation.branchId
            it[salesmanId] = relation.salesmanId
            it[customerId] = relation.customerId
            it[invoiceId] = relation.invoiceId
        }[id].value
    }

//    override fun getIdsOfRelationType(type: EntityType): RelationFilter {
//        return RelationFilter(type)
//    }

    override fun deleteRelationsTypeByCompanyId(companyUID: String, type: EntityType, vararg ids: String): Boolean {
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

//    class RelationFilter(private val requiredType: EntityType) {
//
//        fun filterBy(companyUID: String): List<Int> {
//            return select {
//                (companyId eq companyUID) and (entityType eq requiredType)
//            }.mapNotNull {
//                it.toIdOf(requiredType)
//            }
//        }
//
//        fun filterBy(companyUID: String, filterType: EntityType, filterTypeId: Int): List<Int> {
//            return select {
//                (companyId eq companyUID) and (entityType eq requiredType) and (when (filterType) {
//                    EntityType.COMPANY -> throw IllegalArgumentException("$filterType is not allowed here")
//                    EntityType.BRANCH -> branchId eq filterTypeId
//                    EntityType.SALESMAN -> salesmanId eq filterTypeId
//                    EntityType.CUSTOMER -> customerId eq filterTypeId
//                    EntityType.INVOICE -> invoiceId eq filterTypeId
//                })
//            }.mapNotNull {
//                it.toIdOf(requiredType)
//            }
//        }
//
//        private fun ResultRow.toIdOf(type: EntityType): Int? {
//            return when (type) {
//                EntityType.BRANCH -> this[branchId]
//                EntityType.SALESMAN -> this[salesmanId]
//                EntityType.CUSTOMER -> this[customerId]
//                EntityType.INVOICE -> this[invoiceId]
//                else -> null
//            }
//        }
//    }
}

