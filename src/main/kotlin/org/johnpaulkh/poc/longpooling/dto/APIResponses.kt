package org.johnpaulkh.poc.longpooling.dto

import org.johnpaulkh.poc.longpooling.ex.ServiceException

data class APIResponse<T: Any?>(
    val success: Boolean,
    val data: T,
    val error: ErrorResponse?
) {
    companion object {
        fun<T> success(data: T) = APIResponse(
            success = true,
            data = data,
            error = null,
        )

        fun error(ex: ServiceException) = APIResponse(
            success = false,
            data = null,
            error = ErrorResponse.fromServiceException(ex)
        )

        fun error(ex: Exception) = APIResponse(
            success = false,
            data = null,
            error = ErrorResponse.fromException(ex)
        )
    }
}