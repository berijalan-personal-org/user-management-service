package com.batch14.usermanagementservice.exceptions

class CustomException (
    val exceptionMessage: String,
    val statusCode: Int,
    val data: Any? = null
) : RuntimeException()