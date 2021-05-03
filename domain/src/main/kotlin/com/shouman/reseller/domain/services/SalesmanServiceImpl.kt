package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repositories.RelationRepository
import com.shouman.reseller.domain.repositories.SalesmanRepository

internal class SalesmanServiceImpl(
    private val relationRepository: RelationRepository,
    private val salesmanRepository: SalesmanRepository
) : SalesmanService {

    override fun createSalesman(companyUID: String, branchId: Int, salesman: Salesman): Int {
        return salesmanRepository.add(salesman).also {
            relationRepository.createRelation(
                Relation(
                    type = EntityType.SALESMAN,
                    companyId = companyUID,
                    branchId = branchId,
                    salesmanId = it
                )
            )
        }
    }

    override fun getSalesmanById(id: Int): Salesman? {
        return salesmanRepository.getSalesmanById(id)
    }

    override fun getSalesmanByEmail(email: String): Salesman? {
        return salesmanRepository.getSalesmanByEmail(email)
    }

    override fun getSalesmanBySimNumber(simNumber: String): Salesman? {
        return salesmanRepository.getSalesmanBySimNumber(simNumber)
    }

    override fun getSalesmanByIMEI(imei: Long): Salesman? {
        return salesmanRepository.getSalesmanByIMEI(imei)
    }

    override fun getBranchSalesmen(branchId: Int, lastId: Int, size: Int): List<Salesman> {
        return salesmanRepository.getBranchSalesmen(branchId, lastId, size)
    }

    override fun getCompanySalesmen(companyUID: String, lastId: Int, size: Int): List<SalesmanBranch> {
        return salesmanRepository.getCompanySalesmen(companyUID, lastId, size)
    }

    override fun updateSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean {
        return salesmanRepository.updateSalesman(salesmanId, putSalesman)
    }

    override fun deleteSalesman(companyUID: String, salesmanId: Int): Boolean {
        return relationRepository.deleteRelation(companyUID, EntityType.SALESMAN, salesmanId).also {
            salesmanRepository.deleteSalesman(salesmanId)
        }
    }
}