package com.reseller.ars.domain.datasource.database.dao

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.License
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

interface CompanyDao : BaseDao<String, Company> {

    fun disable(companyUID: String): Boolean

    fun updateLicense(companyUID: String, license: License): Boolean

    fun isEnabled(companyUID: String): Boolean
}

object CompanyDaoImpl : IntIdTable(), CompanyDao {

    val createdAt = long("created_at").default(System.currentTimeMillis())
    val updatedAt = long("updated_at").default(System.currentTimeMillis())
    val uid = varchar("company_id", 200).uniqueIndex()
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

    override fun insert(obj: Company): String {
        return insert {
            it[uid] = obj.uid
            it[ownerName] = obj.ownerName
            it[ownerId] = obj.ownerId
            it[ownerPhone] = obj.ownerPhone
            it[ownerMail] = obj.ownerMail
            it[name] = obj.name
            it[email] = obj.email
            it[city] = obj.city
            it[country] = obj.country
            it[licenseExpire] = obj.licenseExpire
        }[uid]
    }

    override fun selectById(id: String): Company? {
        return select {
            (uid eq id)
        }.mapNotNull {
            it.mapRowToCompany()
        }.singleOrNull()
    }

    override fun delete(id: String): Boolean {
        return deleteWhere { (uid eq id) } > 0
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

    override fun disable(companyUID: String): Boolean {
        return update({ uid eq companyUID }) {
            it[updatedAt] = System.currentTimeMillis()
            it[enabled] = false
        } > 0
    }

    override fun updateLicense(companyUID: String, license: License): Boolean {
        return update({ uid eq companyUID }) {
            it[updatedAt] = System.currentTimeMillis()
            it[licenseExpire] = license.licenseExpire
            it[enabled] = license.isEnabled
        } > 0
    }

    override fun isEnabled(companyUID: String): Boolean {
        val isEnabled: Boolean? = slice(enabled)
            .select {
                (uid eq companyUID)
            }.mapNotNull {
                it[enabled]
            }.singleOrNull()

        return isEnabled ?: false
    }
}