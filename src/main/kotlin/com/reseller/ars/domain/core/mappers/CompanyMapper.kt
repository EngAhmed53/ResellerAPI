package com.reseller.ars.domain.core.mappers

import com.reseller.ars.data.model.Company
import com.reseller.ars.data.model.ResponseCompany


fun Company.toResponseCompany() = ResponseCompany(
    this.ownerName,
    this.ownerMail,
    this.ownerPhone,
    this.name,
    this.email,
    this.city,
    this.country,
    this.enabled,
    this.licenseExpire
)