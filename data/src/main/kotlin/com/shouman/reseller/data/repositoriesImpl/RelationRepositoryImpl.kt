package com.shouman.reseller.data.repositoriesImpl

import com.shouman.reseller.data.datasource.database.dao.RelationDao
import com.shouman.reseller.domain.entities.EntityType
import com.shouman.reseller.domain.entities.Relation
import com.shouman.reseller.domain.repository.RelationRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class RelationRepositoryImpl : RelationRepository, KoinComponent {

    private val relationDao by inject<RelationDao>()

    override fun createRelation(relation: Relation): Int {
       return relationDao.insert(relation)
    }

    override fun deleteRelation(companyUId: String, type: EntityType, vararg relationId: Int): Boolean{
        return relationDao.deleteByCompanyId(companyUId, type, *relationId)
    }
}