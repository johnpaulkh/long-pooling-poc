package org.johnpaulkh.poc.longpooling.service.job

import org.johnpaulkh.poc.longpooling.dto.JobRequest
import org.johnpaulkh.poc.longpooling.dto.JobResponse
import org.johnpaulkh.poc.longpooling.ex.ServiceException
import org.johnpaulkh.poc.longpooling.repository.JobRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.support.CronExpression
import org.springframework.stereotype.Service

@Service
class JobService(
    private val jobRepository: JobRepository
) {

    fun create(request: JobRequest): JobResponse =
        request
            .also { validateCreate(it) }
            .toEntity()
            .let { jobRepository.save(it) }
            .let { JobResponse.fromEntity(it) }

    fun detail(id: String) = jobRepository.findByIdOrNull(id)
        ?.let { JobResponse.fromEntity(it) }
        ?: throw ServiceException(
            code = "JOB_DETAIL__NOT_FOUND",
            message = "Job with id $id cannot be found"
        )

    fun list(clientId: String, page: Int, size: Int): List<JobResponse> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pageRequest = PageRequest.of(page - 1, size, sort)


        val result = jobRepository.findAll(pageRequest)
        return result.map(JobResponse.Companion::fromEntity).toList()
    }

    fun delete(id: String): Boolean {
        jobRepository.deleteById(id)
        return true
    }

    private fun validateCreate(request: JobRequest) {
        val nameExists = jobRepository.existsByClientIdAndName(
            clientId = request.clientId,
            name = request.name,
        )
        if (nameExists) {
            throw ServiceException(
                code = "JOB_CREATE__NAME_EXISTS",
                message = "Job with name '${request.name}' already exists",
            )
        }

        val isCronValid = request.cron.isNullOrBlank() || CronExpression.isValidExpression(request.cron)
        if (!isCronValid) {
            throw ServiceException(
                code = "JOB_CREATE__CRON_INVALID",
                message = "Invalid cron format for '${request.cron}'"
            )
        }

        val callbackRequestNotNull = request.callbackRequest != null
        val callbackEventNotNull = request.callbackEvent != null
        val isCallBackSettingValid = callbackRequestNotNull xor callbackEventNotNull
        if (!isCallBackSettingValid) {
            throw ServiceException(
                code = "JOB_CREATE__CALLBACK_SETTING_INVALID",
                message = """
                    Invalid callback setting format 
                        for httpRequest : '${request.callbackRequest}' 
                        and event : '${request.callbackEvent}
                """.trimIndent()
            )
        }
    }
}