package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.PutSalesman
import com.reseller.ars.data.model.ResponseSalesman
import com.reseller.ars.data.model.Salesman
import com.reseller.ars.domain.datasource.database.dao.SalesmanDao
import org.koin.core.KoinComponent
import org.koin.core.inject

class SalesmanRepositoryImpl : SalesmanRepository, KoinComponent {

    private val salesmanDao by inject<SalesmanDao>()

    override fun addSalesman(salesman: Salesman): Int {
        return salesmanDao.insertSalesman(salesman)
    }

    override fun getCompanySalesmen(companyUID: String, lastID: Int, size: Int): List<ResponseSalesman> {
        return salesmanDao.selectSalesmenByCompanyUID(companyUID, lastID, size)
    }

    override fun getBranchSalesmen(branchId: Int, lastID: Int, size: Int): List<ResponseSalesman> {
        return salesmanDao.selectSalesmenByBranchId(branchId, lastID, size)
    }

    override fun getSalesmanById(salesmanId: Int): ResponseSalesman? {
        return salesmanDao.selectSalesmenById(salesmanId)
    }

    override fun getSalesmanByNationalId(nationalId: String): ResponseSalesman? {
        return salesmanDao.selectSalesmanByNationalId(nationalId)
    }

    override fun getSalesmanByEmail(email: String): ResponseSalesman? {
        return salesmanDao.selectSalesmenByEmail(email)
    }

    override fun getSalesmanBuSimNumber(number: String): ResponseSalesman? {
        return salesmanDao.selectSalesmanBySimNumber(number)
    }

    override fun getSalesmanByIMEI(imei: Long): ResponseSalesman? {
        return salesmanDao.selectSalesmanByIMEI(imei)
    }

    override fun isSalesmanEnabled(salesmanId: Int): Boolean {
        return salesmanDao.isSalesmanEnabled(salesmanId)
    }

    override fun editSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean {
        return salesmanDao.updateSalesman(salesmanId, putSalesman)
    }

    override fun deleteSalesman(salesmanId: Int): Boolean {
        return salesmanDao.deleteSalesman(salesmanId)
    }
}