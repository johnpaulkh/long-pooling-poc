package org.johnpaulkh.poc.longpooling.controller

import org.johnpaulkh.poc.longpooling.dto.JobRequest
import org.johnpaulkh.poc.longpooling.dto.JobPatchRequest
import org.johnpaulkh.poc.longpooling.service.job.JobService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class JobController(
    private val jobService: JobService
) {

    @PostMapping("/jobs")
    fun create(@RequestBody request: JobRequest) = jobService.create(request)

    @PatchMapping("/jobs/{id}")
    fun patch(@PathVariable id: String, @RequestBody request: JobPatchRequest) = jobService.patch(id, request)

    @GetMapping("/jobs/{id}")
    fun detail(@PathVariable id: String) = jobService.detail(id)

    @GetMapping("/clients/{clientId}/jobs")
    fun list(
        @PathVariable clientId: String,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ) = jobService.list(clientId, page, size)

    @DeleteMapping("/jobs/{id}")
    fun delete(@PathVariable id: String) = jobService.delete(id)
}