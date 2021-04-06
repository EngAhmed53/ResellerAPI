package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.PutSalesman
import com.reseller.ars.data.model.Salesman
import com.reseller.ars.data.model.SalesmanBranch

interface SalesmanRepository {
    fun add(salesman: Salesman): Int

    fun getCompanySalesmen(companyUID: String, lastID: Int, size: Int): List<SalesmanBranch>

    fun getBranchSalesmen(branchId: Int, lastID: Int, size: Int): List<Salesman>

    fun getSalesmanById(salesmanId: Int): Salesman?

    fun getSalesmanByNationalId(nationalId: String): Salesman?

    fun getSalesmanByEmail(email: String): Salesman?

    fun getSalesmanBySimNumber(number: String): Salesman?

    fun getSalesmanByIMEI(imei: Long): Salesman?

    fun isSalesmanEnabled(salesmanId: Int): Boolean

    fun updateSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean

    fun deleteSalesman(salesmanId: Int): Boolean
}