package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.PutSalesman
import com.reseller.ars.data.model.Salesman
import com.reseller.ars.data.model.SalesmanBranch

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