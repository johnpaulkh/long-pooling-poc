package org.johnpaulkh.poc.longpooling.service.execution

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.withContext
import org.johnpaulkh.poc.longpooling.config.DispatcherProvider
import org.johnpaulkh.poc.longpooling.entity.EventSetting
import org.johnpaulkh.poc.longpooling.entity.Job
import org.slf4j.Logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

abstract class ExecutionService(
    protected val restTemplate: RestTemplate,
    protected val dispatcherProvider: DispatcherProvider,
    protected val logger: Logger,
    private val objectMapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    abstract suspend fun execute(job: Job): Any?

    private suspend fun call(url: String, method: String, httpEntity: HttpEntity<out Any?>?) =
        withContext(dispatcherProvider.io()) {
            restTemplate.exchange(
                url,
                HttpMethod.valueOf(method),
                httpEntity,
                String::class.java
            )
        }

    suspend fun callExternalAndCallBack(job: Job, preflightResponse: Any?) {
        logger.debug("Call external and callback for entity : {}", preflightResponse)
        val externalUrl = job.externalRequest.url
        val externalCall = when (val externalMethod = job.externalRequest.method) {
            HttpMethod.POST.name() -> callPost(externalUrl, externalMethod, preflightResponse)
            HttpMethod.GET.name() -> callGet(externalUrl, externalMethod, preflightResponse)
            else -> suspend { null }
        }

        val externalCallResult = externalCall.invoke()?.body

        if (job.callBackRequest != null) {
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val httpEntity = HttpEntity<String>(externalCallResult, headers)

            val callbackRequest = job.callBackRequest
            call(callbackRequest.url, callbackRequest.method, httpEntity)
        } else if (job.callBackEvent != null) {
            sendEvent(job.callBackEvent, externalCallResult)
        }
    }

    private fun callPost(
        externalUrl: String,
        externalMethod: String,
        preflightResponse: Any?
    ) = suspend {
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val externalHttpEntity = HttpEntity<Any>(preflightResponse, headers)
        call(externalUrl, externalMethod, externalHttpEntity)
    }

    private fun callGet(
        externalUrl: String,
        externalMethod: String,
        preflightResponse: Any?
    ) = suspend {
        val preflightObj = when (preflightResponse) {
            is String -> objectMapper.readValue<Map<String, Any>>(preflightResponse)
            else -> objectMapper.convertValue<Map<String, Any>>(preflightResponse)
        }
        val uri = preflightObj
            .entries
            .fold(UriComponentsBuilder.fromUri(URI(externalUrl))) { u, o -> u.queryParam(o.key, o.value) }
            ?: UriComponentsBuilder.fromUri(URI(externalUrl))

        call(uri.encode().toUriString(), externalMethod, null)
    }

    private fun sendEvent(eventSetting: EventSetting, externalCallResult: Any?) {
        val topic = eventSetting.topic
        val idPath = eventSetting.idPath
        val key: String
        val message: String

        when(externalCallResult) {
            is String -> {
                val deserialized = objectMapper.readValue<Map<String, Any>>(externalCallResult)
                key = deserialized[idPath].toString()
                message = externalCallResult
            }
            else -> {
                val deserialized = objectMapper.convertValue<Map<String, Any>>(externalCallResult)
                key = deserialized[idPath].toString()
                message = objectMapper.writeValueAsString(externalCallResult)
            }
        }
        kafkaTemplate.send(topic, key, message)
    }
}