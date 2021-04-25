package com.shouman.reseller.domain.repositories

import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.Relation

interface RelationRepository {
    fun createRelation(relation: Relation): Int

    fun getRelation(companyUId: String, type: EntityType, id: Int): Relation?

    fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: Int): Boolean
}