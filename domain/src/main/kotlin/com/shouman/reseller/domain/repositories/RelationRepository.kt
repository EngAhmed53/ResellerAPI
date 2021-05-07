package com.shouman.reseller.domain.repositories

import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.Relation

interface RelationRepository {
    fun createRelation(relation: Relation): Int

    fun isValidRelation(relation: Relation): Boolean

    fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: Int): Boolean
}