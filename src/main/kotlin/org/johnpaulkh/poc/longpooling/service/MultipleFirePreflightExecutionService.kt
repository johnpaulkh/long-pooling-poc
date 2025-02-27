package org.johnpaulkh.poc.longpooling.service

import org.johnpaulkh.poc.longpooling.entity.Job
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MultipleFirePreflightExecutionService: ExecutionService {

    private val logger = LoggerFactory.getLogger(MultipleFirePreflightExecutionService::class.java)

    override fun execute(job: Job) {
        logger.debug("Multiple fire preflight execution")
    }
}