package org.johnpaulkh.poc.longpooling.service.execution.strategies

import kotlinx.coroutines.*
import org.johnpaulkh.poc.longpooling.config.DispatcherProvider
import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.service.execution.ExecutionService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class MultipleFirePreflightExecutionService(
    restTemplate: RestTemplate,
    dispatcherProvider: DispatcherProvider,
) : ExecutionService(
    restTemplate,
    dispatcherProvider,
    LoggerFactory.getLogger(MultipleFirePreflightExecutionService::class.java)
) {

    override suspend fun execute(job: Job) = coroutineScope {
        logger.debug("Multiple fire preflight execution")

        val preflight = job.preFlight!!
        val preflightResponse = withContext(dispatcherProvider.io()) {
            restTemplate.getForEntity(preflight.url, List::class.java)
                .body
        }

        preflightResponse?.forEach { entry ->
            handlePreflightResponse(job, entry)
        }
    }

    fun CoroutineScope.handlePreflightResponse(job: Job, entry: Any?) = launch(dispatcherProvider.io()) {
        if (entry == null)
            return@launch

        logger.debug("Thread name : ${Thread.currentThread().name}")
        val externalHttpEntity = HttpEntity<Any>(entry, null)
        callExternalAndCallBack(job, externalHttpEntity)
    }
}