package org.johnpaulkh.poc.longpooling.config

import org.johnpaulkh.poc.longpooling.entity.JobType
import org.johnpaulkh.poc.longpooling.service.MultipleFirePreflightExecutionService
import org.johnpaulkh.poc.longpooling.service.SingleFireExecutionService
import org.johnpaulkh.poc.longpooling.service.SingleFirePreflightExecutionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExecutionConfig(
    private val singleFireExecutionService: SingleFireExecutionService,
    private val singleFirePreflightExecutionService: SingleFirePreflightExecutionService,
    private val multipleFirePreflightExecutionService: MultipleFirePreflightExecutionService,
) {

    @Bean
    fun executionServiceMap() = mapOf(
        JobType.SINGLE_FIRE to singleFireExecutionService,
        JobType.SINGLE_FIRE_PREFLIGHT to singleFirePreflightExecutionService,
        JobType.MULTIPLE_FIRE_PREFLIGHT to multipleFirePreflightExecutionService,
    )
}