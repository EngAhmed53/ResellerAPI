package com.shouman.reseller.domain.repository

import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.Relation

interface RelationRepository {
    fun createRelation(relation: Relation): Int
    fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: Int): Boolean
}