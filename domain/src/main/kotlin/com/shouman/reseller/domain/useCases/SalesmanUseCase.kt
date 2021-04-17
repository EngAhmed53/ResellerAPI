package com.shouman.reseller.domain.useCases

import com.shouman.reseller.domain.core.BaseUseCase
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.repository.RelationRepository
import com.shouman.reseller.domain.repository.SalesmanRepository
import org.koin.core.inject


class CreateSalesmanUseCase(
    private val companyUID: String,
    private val branchId: Int,
    private val salesman: Salesman
) : BaseUseCase<Int> {

    private val salesmanRepository by inject<SalesmanRepository>()
    private val relationRepository by inject<RelationRepository>()

    override fun invoke(): Int {
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
}

class GetSalesmanByIdUseCase(
    private val id: Int
): BaseUseCase<Salesman?> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): Salesman? {
        return salesmanRepository.getSalesmanById(id)
    }
}

class GetSalesmanByEmailUseCase(
    private val email: String
): BaseUseCase<Salesman?> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): Salesman? {
        return salesmanRepository.getSalesmanByEmail(email)
    }
}

class GetSalesmanByNationalIdUseCase(
    private val nationalId: String
): BaseUseCase<Salesman?> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): Salesman? {
        return salesmanRepository.getSalesmanByNationalId(nationalId)
    }
}

class GetSalesmanBySimNumberUseCase(
    private val simNumber: String
): BaseUseCase<Salesman?> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): Salesman? {
        return salesmanRepository.getSalesmanBySimNumber(simNumber)
    }
}

class GetSalesmanByIMEIUseCase(
    private val imei: Long
): BaseUseCase<Salesman?> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): Salesman? {
        return salesmanRepository.getSalesmanByIMEI(imei)
    }
}

class GetBranchSalesmenUseCase(
    private val branchId: Int,
    private val lastId: Int,
    private val size: Int
): BaseUseCase<List<Salesman>> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): List<Salesman> {
        return salesmanRepository.getBranchSalesmen(branchId, lastId, size)
    }
}

class GetCompanySalesmenBranchUseCase(
    private val companyUID: String,
    private val lastId: Int,
    private val size: Int
): BaseUseCase<List<SalesmanBranch>> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): List<SalesmanBranch> {
        return salesmanRepository.getCompanySalesmen(companyUID, lastId, size)
    }
}

class UpdateSalesmanInfoUseCase(
    private val id: Int,
    private val putSalesman: PutSalesman
): BaseUseCase<Boolean> {

    private val salesmanRepository by inject<SalesmanRepository>()

    override fun invoke(): Boolean{
        return salesmanRepository.updateSalesman(id, putSalesman)
    }

}

class DeleteSalesmanUseCase(
    private val companyUID: String,
    private val salesmanId: Int
): BaseUseCase<Boolean> {

    private val salesmanRepository by inject<SalesmanRepository>()
    private val relationRepository by inject<RelationRepository>()

    override fun invoke(): Boolean {
        return salesmanRepository.deleteSalesman(salesmanId).also {
            relationRepository.deleteRelation(companyUID, EntityType.SALESMAN, salesmanId)
        }
    }
}