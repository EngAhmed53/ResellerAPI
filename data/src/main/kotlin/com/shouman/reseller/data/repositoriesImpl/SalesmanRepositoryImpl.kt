package com.shouman.reseller.data.repositoriesImpl


import com.shouman.reseller.data.datasource.database.dao.SalesmanDao
import com.shouman.reseller.domain.repositories.SalesmanRepository
import com.shouman.reseller.domain.entities.PutSalesman
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.SalesmanBranch
import org.koin.core.KoinComponent
import org.koin.core.inject

class SalesmanRepositoryImpl : SalesmanRepository, KoinComponent {

    private val salesmanDao by inject<SalesmanDao>()

    override fun add(salesman: Salesman): Int {
        return salesmanDao.insert(salesman)
    }

    override fun getCompanySalesmen(companyUID: String, lastID: Int, size: Int): List<SalesmanBranch> {
        return salesmanDao.selectByCompanyUID(companyUID, lastID, size)
    }

    override fun getBranchSalesmen(branchId: Int, lastID: Int, size: Int): List<Salesman> {
        return salesmanDao.selectByBranchId(branchId, lastID, size)
    }

    override fun getSalesmanById(salesmanId: Int): Salesman? {
        return salesmanDao.selectById(salesmanId)
    }

    override fun getSalesmanByNationalId(nationalId: String): Salesman? {
        return salesmanDao.selectByNationalId(nationalId)
    }

    override fun getSalesmanByEmail(email: String): Salesman? {
        return salesmanDao.selectByEmail(email)
    }

    override fun getSalesmanBySimNumber(number: String): Salesman? {
        return salesmanDao.selectBySimNumber(number)
    }

    override fun getSalesmanByIMEI(imei: Long): Salesman? {
        return salesmanDao.selectByIMEI(imei)
    }

    override fun isSalesmanEnabled(salesmanId: Int): Boolean {
        return salesmanDao.isEnabled(salesmanId)
    }

    override fun updateSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean {
        return salesmanDao.update(salesmanId, putSalesman)
    }

    override fun deleteSalesman(salesmanId: Int): Boolean {
        return salesmanDao.delete(salesmanId)
    }
}