package org.johnpaulkh.poc.longpooling.service

import org.johnpaulkh.poc.longpooling.entity.Job
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class SingleFireExecutionService(
    private val restTemplate: RestTemplate
): ExecutionService {

    private val logger = LoggerFactory.getLogger(SingleFireExecutionService::class.java)

    override fun execute(job: Job) {
        logger.debug("Single fire execution")

        val externalRequest = job.externalRequest

        val externalHttpEntity = HttpEntity<String>(null, null)
        val externalResponse = restTemplate.exchange(
            externalRequest.url,
            HttpMethod.valueOf(externalRequest.method),
            externalHttpEntity,
            String::class.java
        )

        val callbackRequest = job.callBackRequest
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val callbackHttpEntity = HttpEntity<String>(externalResponse.body, headers)
        restTemplate.exchange(
            callbackRequest.url,
            HttpMethod.valueOf(callbackRequest.method),
            callbackHttpEntity,
            String::class.java
        )
    }
}