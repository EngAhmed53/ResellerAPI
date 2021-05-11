package com.shouman.reseller.controller

import com.shouman.reseller.Response
import com.shouman.reseller.domain.core.mappers.toResponseBranch
import com.shouman.reseller.domain.entities.*
import com.shouman.reseller.domain.entities.StatusCode.*
import com.shouman.reseller.domain.services.*
import io.ktor.http.*

class BranchController(
    private val branchService: BranchService
) : BaseController() {

    suspend fun creatCompanyBranch(companyUID: String, branch: Branch): Response<Int> = dbQuery {
        when (val id = branchService.createBranch(companyUID, branch)) {
            -1 -> HttpStatusCode.Locked to ServerResponse(statusCode = COMPANY_DISABLED)
            else -> HttpStatusCode.OK to ServerResponse(body = id)
        }
    }

    suspend fun getBranchById(branchId: Int): Response<ResponseBranch> = dbQuery {
        branchService.getBranchById(branchId)?.let {
            HttpStatusCode.OK to ServerResponse(body = it.toResponseBranch())
        } ?: HttpStatusCode.NotFound to ServerResponse(statusCode = NOT_FOUND)
    }

    suspend fun getCompanyBranches(
        companyUID: String,
        lastId: Int,
        size: Int
    ): Response<List<ResponseBranch>> =
        dbQuery {
            val result  = branchService.getCompanyBranches(companyUID, lastId, size)
            when(result.first) {
                INVALID_RELATION -> HttpStatusCode.NotFound to ServerResponse(statusCode = NOT_FOUND)
                SUCCESS -> {
                    HttpStatusCode.OK to ServerResponse(body = result.second.map { it.toResponseBranch() })
                }
                else -> throw Exception("Illegal status code ${result.first}")
            }

        }

    suspend fun updateBranchInfo(
        companyUID: String,
        branchId: Int,
        putBranch: PutBranch
    ): Response<Unit> = dbQuery {
        return@dbQuery when (val code = branchService.updateBranch(companyUID, branchId, putBranch)) {
            INVALID_RELATION -> {
                HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
            }
            UPDATE_ERROR -> {
                HttpStatusCode.BadRequest to ServerResponse(statusCode = UPDATE_ERROR)
            }
            SUCCESS -> {
                HttpStatusCode.OK to ServerResponse(statusCode = SUCCESS)
            }
            else -> throw Exception("$code is not a valid state here")
        }
    }

    suspend fun deleteCompanyBranchById(companyUID: String, branchId: Int): Response<Unit> = dbQuery {

        return@dbQuery when (val code = branchService.deleteBranch(companyUID, branchId)) {
            INVALID_RELATION -> {
                HttpStatusCode.NotFound to ServerResponse(statusCode = ERROR)
            }
            DELETE_ERROR -> {
                HttpStatusCode.BadRequest to ServerResponse(statusCode = DELETE_ERROR)
            }
            SUCCESS -> {
                HttpStatusCode.OK to ServerResponse(statusCode = SUCCESS)
            }
            else -> throw Exception("$code is not a valid state here")
        }
    }
}