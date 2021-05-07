package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.Relation
import com.shouman.reseller.domain.repositories.RelationRepository

class RelationServiceImpl(private val relationRepository: RelationRepository) : RelationService {
    override fun isValidRelation(relation: Relation): Boolean {
        return relationRepository.isValidRelation(relation)
    }
}