package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.Relation

interface RelationRepository {
    fun createRelation(relation: Relation): Int
    fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: String): Boolean
}