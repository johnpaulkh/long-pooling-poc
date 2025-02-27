package org.johnpaulkh.poc.longpooling.ex

data class ServiceException(
    val code: String,
    override val message: String,
): RuntimeException(message)