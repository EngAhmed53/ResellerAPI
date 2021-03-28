package com.reseller.ars.data.database.dao

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.License
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

interface CompanyDao {
    fun insertCompany(pushCompany: Company): Int

    fun getCompanyByUID(companyUID: String): Company?

    fun disableCompany(companyUID: String): Company?

    fun extendCompanyLicense(companyUID: String, license: License): Company?
}

class CompanyDaoImpl : IntIdTable(), CompanyDao {

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

    override fun insertCompany(pushCompany: Company): Int {
        return insert {
            it[uid] = pushCompany.uid
            it[ownerName] = pushCompany.ownerName
            it[ownerId] = pushCompany.ownerId
            it[ownerPhone] = pushCompany.ownerPhone
            it[ownerMail] = pushCompany.ownerMail
            it[name] = pushCompany.name
            it[email] = pushCompany.email
            it[city] = pushCompany.city
            it[country] = pushCompany.country
            it[licenseExpire] = pushCompany.licenseExpire
        }[id].value
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

    override fun disableCompany(companyUID: String): Company? {
        update({ uid eq companyUID }) {
            it[updatedAt] = System.currentTimeMillis()
            it[enabled] = false
        }

        return getCompanyByUID(companyUID)
    }

    override fun extendCompanyLicense(companyUID: String, license: License): Company? {
        update({ uid eq companyUID }) {
            it[updatedAt] = System.currentTimeMillis()
            it[licenseExpire] = license.licenseExpire
            it[enabled] = license.enabled
        }

        return getCompanyByUID(companyUID)
    }
}