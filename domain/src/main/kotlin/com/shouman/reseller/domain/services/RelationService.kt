package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.Relation

interface RelationService {

    fun addRelation(relation: Relation): Int

    fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: Int): Boolean

    fun isValidRelation(relation: Relation): Boolean
}

