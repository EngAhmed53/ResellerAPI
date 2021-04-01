package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.EntityType
import com.reseller.ars.data.model.Relation
import com.reseller.ars.domain.datasource.database.dao.RelationDao
import org.koin.core.KoinComponent
import org.koin.core.inject

class RelationRepositoryImpl : RelationRepository, KoinComponent {

    private val relationDao by inject<RelationDao>()

    override fun createRelation(relation: Relation): Int {
       return relationDao.createNewRelation(relation)
    }

    override fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: String): Boolean{
        return relationDao.deleteRelationsTypeByCompanyId(companyUId, type, *relationId)
    }
}