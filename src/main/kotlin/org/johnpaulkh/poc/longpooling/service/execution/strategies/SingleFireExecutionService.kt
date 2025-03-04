package org.johnpaulkh.poc.longpooling.service.execution.strategies

import org.johnpaulkh.poc.longpooling.config.DispatcherProvider
import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.service.execution.ExecutionService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SingleFireExecutionService(
    restTemplate: RestTemplate,
    dispatcherProvider: DispatcherProvider
) : ExecutionService(restTemplate, dispatcherProvider) {

    private val logger = LoggerFactory.getLogger(SingleFireExecutionService::class.java)

    override suspend fun execute(job: Job) {
        logger.debug("Single fire execution")
        val externalHttpEntity = HttpEntity<String>(null, null)
        callExternalAndCallBack(job, externalHttpEntity)
    }
}