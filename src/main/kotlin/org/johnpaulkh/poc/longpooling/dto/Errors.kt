package org.johnpaulkh.poc.longpooling.dto

import org.johnpaulkh.poc.longpooling.ex.ServiceException
import org.springframework.web.bind.annotation.ExceptionHandler

data class ErrorResponse(
    val code: String,
    val message: String?,
) {
    companion object {
        fun fromServiceException(ex: ServiceException) = ErrorResponse(
            code = ex.code,
            message = ex.message,
        )

        fun fromException(ex: Exception) = ErrorResponse(
            code = "UNSPECIFIED",
            message = ex.message,
        )
    }
}