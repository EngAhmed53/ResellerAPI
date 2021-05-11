package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.Relation
import com.shouman.reseller.domain.repositories.RelationRepository

class RelationServiceImpl(private val relationRepository: RelationRepository) : RelationService {

    override fun addRelation(relation: Relation): Int {
        return relationRepository.createRelation(relation)
    }

    override fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: Int): Boolean {
        return relationRepository.deleteRelation(companyUId, type, *relationId)
    }

    override fun isValidRelation(relation: Relation): Boolean {
        return relationRepository.isValidRelation(relation)
    }
}