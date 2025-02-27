package org.johnpaulkh.poc.longpooling.config

import org.johnpaulkh.poc.longpooling.dto.APIResponse
import org.johnpaulkh.poc.longpooling.ex.ServiceException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(exception = [ServiceException::class])
    fun serviceExceptionHandler(ex: ServiceException) =
        ResponseEntity.unprocessableEntity().body(APIResponse.error(ex))

    @ExceptionHandler(exception = [Exception::class])
    fun exceptionHandler(ex: Exception) =
        ResponseEntity.internalServerError().body(APIResponse.error(ex))

}