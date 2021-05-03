package com.shouman.reseller.domain.core.mappers

import com.shouman.reseller.domain.entities.Company
import com.shouman.reseller.domain.entities.ResponseCompany


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