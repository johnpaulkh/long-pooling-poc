package org.johnpaulkh.poc.longpooling.service.execution

import org.johnpaulkh.poc.longpooling.dto.ExecutionRequest
import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.entity.JobType
import org.johnpaulkh.poc.longpooling.ex.ServiceException
import org.johnpaulkh.poc.longpooling.repository.JobRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ExecutionCoordinatorService(
    private val jobRepository: JobRepository,
    private val executionServiceMap: Map<JobType, ExecutionService>
) {

    fun execute(
        request: ExecutionRequest
    ) {
        val job = jobRepository.findByClientIdAndName(
            clientId = request.clientId,
            name = request.name
        ) ?: throw ServiceException(
            code = "EXECUTION__NO_JOB",
            message = "Job for ${request.clientId} with name ${request.name} cannot be found",
        )
        execute(job)
    }

    fun execute(jobId: String) {
        val job = jobRepository.findByIdOrNull(jobId)
            ?: throw ServiceException(
                code = "EXECUTION__NO_JOB",
                message = "Job with id $jobId cannot be found",
            )
        execute(job)
    }

    fun execute(job: Job) {
        val jobType = job.type
        val executionService = executionServiceMap[jobType]
            ?: throw ServiceException(
                code = "EXECUTION__NO_EXECUTION_SERVICE_AVAILABLE",
                message = "Execution service for type $jobType cannot be found",
            )

        executionService.execute(job)
    }
}