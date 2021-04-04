package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.PutSalesman
import com.reseller.ars.data.model.ResponseSalesman
import com.reseller.ars.data.model.Salesman

interface SalesmanDao {

    fun insertSalesman(salesman: Salesman): Int

    fun selectSalesmenByCompanyUID(companyUID: String, lastId: Int, size: Int): List<ResponseSalesman>

    fun selectSalesmenByBranchId(branchId: Int, lastId: Int, size: Int): List<ResponseSalesman>

    fun selectSalesmenById(id: Int): ResponseSalesman?

    fun selectSalesmenByEmail(email: String): ResponseSalesman?

    fun selectSalesmanByNationalId(nationalId: String): ResponseSalesman?

    fun selectSalesmanBySimNumber(simNumber: String): ResponseSalesman?

    fun selectSalesmanByIMEI(imei: Long): ResponseSalesman?

    fun isSalesmanEnabled(salesmanId: Int): Boolean

    fun updateSalesman(salesmanId: Int, putSalesman: PutSalesman): Boolean

    fun deleteSalesman(salesmanId: Int): Boolean
}