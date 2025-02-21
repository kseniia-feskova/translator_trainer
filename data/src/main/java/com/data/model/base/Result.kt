package com.data.model.base

data class Result<T>(
    val data: T? = null,
    val errorMsg: String = ""
)