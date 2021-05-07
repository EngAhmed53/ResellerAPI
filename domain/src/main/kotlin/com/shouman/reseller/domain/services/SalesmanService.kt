package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*

interface SalesmanService {
    fun createSalesman(companyUID: String, branchId: Int, salesman: Salesman): Int

    fun getSalesmanById(id: Int): Salesman?

    fun getSalesmanByEmail(email: String): Salesman?

    fun getSalesmanBySimNumber(simNumber: String): Salesman?

    fun getSalesmanByIMEI(imei: Long): Salesman?

    fun getBranchSalesmen(branchId: Int, lastId: Int, size: Int): List<Salesman>

    fun getCompanySalesmen(companyUID: String, lastId: Int, size: Int): List<SalesmanBranch>

    fun updateSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean

    fun deleteSalesman(companyUID: String, salesmanId: Int): Boolean
}
