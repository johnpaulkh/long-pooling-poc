package org.johnpaulkh.poc.longpooling.service.execution.strategies

import kotlinx.coroutines.withContext
import org.johnpaulkh.poc.longpooling.config.DispatcherProvider
import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.service.execution.ExecutionService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SingleFirePreflightExecutionService(
    restTemplate: RestTemplate,
    dispatcherProvider: DispatcherProvider,
): ExecutionService(restTemplate, dispatcherProvider) {

    private val logger = LoggerFactory.getLogger(SingleFirePreflightExecutionService::class.java)

    override suspend fun execute(job: Job) {
        logger.debug("Single fire preflight execution")

        val preflight = job.preFlight!!
        val preflightResponse = withContext(dispatcherProvider.io()) {
            restTemplate.getForEntity(preflight.url, String::class.java)
                .body
        }

        val externalHttpEntity = HttpEntity<String>(preflightResponse, null)
        callExternalAndCallBack(job, externalHttpEntity)
    }
}