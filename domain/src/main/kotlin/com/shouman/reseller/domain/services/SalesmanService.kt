package com.shouman.reseller.domain.services

import com.shouman.reseller.domain.entities.*

interface SalesmanService {
    fun createSalesman(companyUID: String, branchId: Int, postSalesman: PostSalesman): Pair<StatusCode, Int>

    fun getSalesmanById(id: Int): Salesman?

    fun getSalesmanByEmail(email: String): Salesman?

    fun getSalesmanBySimNumber(simNumber: String): Salesman?

    fun getSalesmanByIMEI(imei: Long): Salesman?

    fun getBranchSalesmen(
        companyUID: String,
        branchId: Int,
        lastId: Int,
        size: Int
    ): Pair<StatusCode, List<Salesman>>

    fun getCompanySalesmen(companyUID: String, lastId: Int, size: Int): Pair<StatusCode, List<SalesmanBranch>>

    fun updateSalesman(companyUID: String, salesmanId: Int, putSalesman: PutSalesman): StatusCode

    fun deleteSalesman(companyUID: String, salesmanId: Int): StatusCode
}
