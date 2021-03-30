package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.License
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

interface CompanyDao {
    fun insertCompany(company: Company): String

    fun getCompanyByUID(companyUID: String): Company?

    fun disableCompany(companyUID: String): Boolean

    fun extendCompanyLicense(companyUID: String, license: License): Boolean
}

object CompanyDaoImpl : IntIdTable(), CompanyDao {

    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val uid = varchar("company_id", 200)
    val ownerName = varchar("owner_name", 200)
    val ownerId = varchar("owner_id", 200)
    val ownerMail = varchar("owner_email", 200)
    val ownerPhone = varchar("owner_phone", 50)
    val name = varchar("name", 200)
    val email = varchar("email", 200).nullable()
    val city = varchar("city", 100)
    val country = varchar("country", 100)
    val enabled = bool("enabled").default(true)
    val licenseExpire = long("license_expire")

    override fun insertCompany(company: Company): String {
        return insert {
            it[uid] = company.uid
            it[ownerName] = company.ownerName
            it[ownerId] = company.ownerId
            it[ownerPhone] = company.ownerPhone
            it[ownerMail] = company.ownerMail
            it[name] = company.name
            it[email] = company.email
            it[city] = company.city
            it[country] = company.country
            it[licenseExpire] = company.licenseExpire
        }[uid]
    }

    override fun getCompanyByUID(companyUID: String): Company? {
        return select {
            (uid eq companyUID)
        }.mapNotNull {
            it.mapRowToCompany()
        }.singleOrNull()
    }

    private fun ResultRow.mapRowToCompany() =
        Company(
            uid = this[uid],
            ownerName = this[ownerName],
            ownerId = this[ownerId],
            ownerMail = this[ownerMail],
            ownerPhone = this[ownerPhone],
            name = this[name],
            email = this[email],
            city = this[city],
            country = this[country],
            enabled = this[enabled],
            licenseExpire = this[licenseExpire]
        )

    override fun disableCompany(companyUID: String): Boolean {
        return update({ uid eq companyUID }) {
            it[updatedAt] = System.currentTimeMillis()
            it[enabled] = false
        } > 0
    }

    override fun extendCompanyLicense(companyUID: String, license: License): Boolean {
        return update({ uid eq companyUID }) {
            it[updatedAt] = System.currentTimeMillis()
            it[licenseExpire] = license.licenseExpire
            it[enabled] = license.isEnabled
        } > 0
    }
}