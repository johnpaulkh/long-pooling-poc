package org.johnpaulkh.poc.longpooling.service.execution.strategies

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.johnpaulkh.poc.longpooling.config.DispatcherProvider
import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.service.execution.ExecutionService
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class MultipleFirePreflightExecutionService(
    restTemplate: RestTemplate,
    dispatcherProvider: DispatcherProvider,
    objectMapper: ObjectMapper,
    kafkaTemplate: KafkaTemplate<String, String>,
) : ExecutionService(
    restTemplate,
    dispatcherProvider,
    LoggerFactory.getLogger(MultipleFirePreflightExecutionService::class.java),
    objectMapper,
    kafkaTemplate,
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

        callExternalAndCallBack(job, entry)
    }
}