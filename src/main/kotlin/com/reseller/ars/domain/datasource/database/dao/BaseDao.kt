package com.reseller.ars.domain.datasource.database.dao

interface BaseDao<T, E> {

    fun insert(obj: E): T

    fun selectById(id: T): E?

    fun delete(id: T): Boolean
}