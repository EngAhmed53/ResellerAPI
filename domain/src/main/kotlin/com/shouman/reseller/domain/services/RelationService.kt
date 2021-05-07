package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.Relation

interface RelationService {
    fun isValidRelation(relation: Relation): Boolean
}

