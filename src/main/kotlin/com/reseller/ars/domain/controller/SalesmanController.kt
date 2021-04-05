package com.reseller.ars.domain.controller

import com.reseller.ars.data.model.Salesman
import com.reseller.ars.domain.repository.BranchRepository
import com.reseller.ars.domain.repository.CompanyRepository
import com.reseller.ars.domain.repository.RelationRepository
import com.reseller.ars.domain.repository.SalesmanRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class SalesmanController: BaseController(), KoinComponent {

    private val relationRepository by inject<RelationRepository>()
    private val companyRepository by inject<CompanyRepository>()
    private val salesmanRepository by inject<SalesmanRepository>()
    private val branchRepository by inject<BranchRepository>()


//    suspend fun addSalesman(salesman: Salesman): Int = dbQuery {
//
//    }

}