package com.reseller.ars.domain.repository

import com.reseller.ars.data.model.PutSalesman
import com.reseller.ars.data.model.ResponseSalesman
import com.reseller.ars.data.model.Salesman

interface SalesmanRepository {
    fun addSalesman(salesman: Salesman): Int

    fun getCompanySalesmen(companyUID: String, lastID: Int, size: Int): List<ResponseSalesman>

    fun getBranchSalesmen(branchId: Int, lastID: Int, size: Int): List<ResponseSalesman>

    fun getSalesmanById(salesmanId: Int): ResponseSalesman?

    fun getSalesmanByNationalId(nationalId: String): ResponseSalesman?

    fun getSalesmanByEmail(email: String): ResponseSalesman?

    fun getSalesmanBuSimNumber(number: String): ResponseSalesman?

    fun getSalesmanByIMEI(imei: Long): ResponseSalesman?

    fun isSalesmanEnabled(salesmanId: Int): Boolean

    fun editSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean

    fun deleteSalesman(salesmanId: Int): Boolean
}