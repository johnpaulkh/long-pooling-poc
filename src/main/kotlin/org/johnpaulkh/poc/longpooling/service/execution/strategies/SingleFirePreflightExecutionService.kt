package org.johnpaulkh.poc.longpooling.service.execution.strategies

import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.service.execution.ExecutionService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SingleFirePreflightExecutionService: ExecutionService {

    private val logger = LoggerFactory.getLogger(SingleFirePreflightExecutionService::class.java)

    override fun execute(job: Job) {
        logger.debug("Single fire preflight execution")
    }
}