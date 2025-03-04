package org.johnpaulkh.poc.longpooling.controller

import org.johnpaulkh.poc.longpooling.dto.ExecutionRequest
import org.johnpaulkh.poc.longpooling.service.execution.ExecutionCoordinatorService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/executions")
class ExecutionController(
    private val executionService: ExecutionCoordinatorService
) {

    @PostMapping("/{id}")
    suspend fun execute(@PathVariable id: String) = executionService.execute(id)

    @PostMapping
    suspend fun execute(@RequestBody request: ExecutionRequest) = executionService.execute(request)
}