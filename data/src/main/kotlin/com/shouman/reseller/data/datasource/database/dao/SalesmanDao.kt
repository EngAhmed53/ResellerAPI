package com.shouman.reseller.data.datasource.database.dao

import com.shouman.reseller.domain.entities.PutSalesman
import com.shouman.reseller.domain.entities.Salesman
import com.shouman.reseller.domain.entities.SalesmanBranch

interface SalesmanDao: BaseDao<Int, Salesman> {

    fun selectByCompanyUID(companyUID: String, lastId: Int, size: Int): List<SalesmanBranch>

    fun selectByBranchId(branchId: Int, lastId: Int, size: Int): List<Salesman>

    fun selectByEmail(email: String): Salesman?

    fun selectByNationalId(nationalId: String): Salesman?

    fun selectBySimNumber(simNumber: String): Salesman?

    fun selectByIMEI(imei: Long): Salesman?

    fun isEnabled(salesmanId: Int): Boolean

    fun update(salesmanId: Int, putSalesman: PutSalesman): Boolean
}