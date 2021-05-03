package com.shouman.reseller.domain.repositories

import com.shouman.reseller.domain.entities.PutSalesman
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.SalesmanBranch

interface SalesmanRepository {
    fun add(salesman: Salesman): Int

    fun getCompanySalesmen(companyUID: String, lastID: Int, size: Int): List<SalesmanBranch>

    fun getBranchSalesmen(branchId: Int, lastID: Int, size: Int): List<Salesman>

    fun getSalesmanById(salesmanId: Int): Salesman?

    fun getSalesmanByEmail(email: String): Salesman?

    fun getSalesmanBySimNumber(number: String): Salesman?

    fun getSalesmanByIMEI(imei: Long): Salesman?

    fun isSalesmanEnabled(salesmanId: Int): Boolean

    fun updateSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean

    fun deleteSalesman(salesmanId: Int): Boolean
}