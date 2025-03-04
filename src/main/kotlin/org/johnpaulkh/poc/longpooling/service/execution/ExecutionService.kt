package org.johnpaulkh.poc.longpooling.service.execution

import kotlinx.coroutines.withContext
import org.johnpaulkh.poc.longpooling.config.DispatcherProvider
import org.johnpaulkh.poc.longpooling.entity.Job
import org.springframework.http.*
import org.springframework.web.client.RestTemplate

abstract class ExecutionService(
    protected val restTemplate: RestTemplate,
    protected val dispatcherProvider: DispatcherProvider,
) {

    abstract suspend fun execute(job: Job)

    private suspend fun call(url: String, method: String, httpEntity: HttpEntity<out Any>) =
        withContext(dispatcherProvider.io()) {
            restTemplate.exchange(
                url,
                HttpMethod.valueOf(method),
                httpEntity,
                String::class.java
            )
        }

    suspend fun callExternalAndCallBack(job: Job, externalHttpEntity: HttpEntity<out Any>): ResponseEntity<String> {
        val externalUrl = job.externalRequest.url
        val externalMethod = job.externalRequest.method
        return call(externalUrl, externalMethod, externalHttpEntity)
            .body
            .let { body ->
                val headers = HttpHeaders()
                headers.contentType = MediaType.APPLICATION_JSON
                HttpEntity<String>(body, headers)
            }
            .let { httpEntity ->
                val callbackRequest = job.callBackRequest
                call(callbackRequest.url, callbackRequest.method, httpEntity)
            }
    }
}