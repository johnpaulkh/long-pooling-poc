package org.johnpaulkh.poc.longpooling.dto

import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.entity.JobType
import org.johnpaulkh.poc.longpooling.entity.PreFlight
import java.time.Instant

typealias EntityHttpRequestSetting = org.johnpaulkh.poc.longpooling.entity.RequestSetting
typealias EntityEventSetting = org.johnpaulkh.poc.longpooling.entity.EventSetting

data class JobRequest(
    val type: JobType,
    val clientId: String,
    val name: String,
    val cron: String? = null,
    val externalRequest: RequestSetting,
    val callbackRequest: RequestSetting? = null,
    val callbackEvent: EventSetting? = null,
    val preFlight: PreFlightRequest? = null,
) {
    fun toEntity() = Job(
        type = type,
        name = name,
        cron = cron,
        clientId = clientId,
        externalRequest = externalRequest.toEntity(),
        callBackRequest = callbackRequest?.toEntity(),
        callBackEvent = callbackEvent?.toEntity(),
        createdAt = Instant.now(),
        preFlight = preFlight?.toEntity()
    )
}

data class JobPatchRequest(
    val cron: String? = null,
    val externalRequest: RequestSetting? = null,
    val callbackRequest: RequestSetting? = null,
    val callbackEvent: EventSetting? = null,
    val preflight: PreFlightRequest? = null,
)

data class EventSetting(
    val topic: String,
    val idPath: String,
) {
    fun toEntity() = EntityEventSetting(
        topic = topic,
        idPath = idPath,
    )

    companion object {
        fun fromEntity(entity: EntityEventSetting?): EventSetting? = entity?.let {
            EventSetting(
                topic = it.topic,
                idPath = it.idPath,
            )
        }
    }
}

data class RequestSetting(
    val method: String,
    val url: String,
) {
    fun toEntity() = EntityHttpRequestSetting(
        method = method,
        url = url,
    )

    companion object {
        fun fromEntity(entity: EntityHttpRequestSetting?) = entity?.let {
            RequestSetting(
                method = entity.method,
                url = entity.url,
            )
        }
    }
}

data class JobResponse(
    val id: String?,
    val type: String,
    val clientId: String,
    val name: String,
    val cron: String? = null,
    val preFlight: PreFlightRequest? = null,
    val externalRequest: RequestSetting,
    val callbackRequest: RequestSetting? = null,
    val callbackEvent: EventSetting? = null,
    val createdAt: Instant,
) {
    companion object {
        fun fromEntity(job: Job) = JobResponse(
            id = job.id,
            type = job.type.name,
            clientId = job.clientId,
            name = job.name,
            cron = job.cron,
            externalRequest = RequestSetting.fromEntity(job.externalRequest)!!,
            callbackRequest = RequestSetting.fromEntity(job.callBackRequest),
            callbackEvent = EventSetting.fromEntity(job.callBackEvent),
            preFlight = job.preFlight?.let { PreFlightRequest(it.url) },
            createdAt = job.createdAt,
        )
    }
}

data class PreFlightRequest(
    val url: String
) {
    fun toEntity() = PreFlight(url = url)
}