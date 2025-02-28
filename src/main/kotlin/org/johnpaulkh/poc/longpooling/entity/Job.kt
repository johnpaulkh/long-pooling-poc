package org.johnpaulkh.poc.longpooling.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.Instant

@Document("job")
data class Job(
    @MongoId
    var id: String? = null,
    val clientId: String,
    val name: String,
    val cron: String? = null,
    val type: JobType,
    val preFlight: PreFlight? = null,
    val externalRequest: RequestSetting,
    val callBackRequest: RequestSetting,
    val createdAt: Instant,
)

enum class JobType {
    /**
     * Execute the job with single URL with no parameter.
     *
     * Run once per cron job, no pre-flight, simple and pure
     */
    SINGLE_FIRE,

    /**
     * Execute preflight first, then use the response from preflight as the request for the job.
     *
     * Run the job once with data transformation from preflight response.
     * The format must be a single object with corresponding keys and values
     * ```json
     * {
     *   "id": "id-1-value",
     *   "name": "name-1-value"
     * }
     * ```
     * This will result in 1 HTTP request to external.
     * 1. With method GET and format = `https://external.api/v1/{id}?name={name}`
     *     * `https://external.api/v1/id-1?name=name-1-value`
     *
     * 2. With method POST and format = `https://external.api/v1`
     *     * `https://external.api/v1` body:  `{"id": "id-1-value","name": "name-1-value"}`
     *
     * Requires preflight to be registered.
     */
    SINGLE_FIRE_PREFLIGHT,

    /**
     * Execute preflight first, then use the response from preflight as the request for the job.
     *
     * Run the job once with expected data format as its response.
     * The format must be list of object with corresponding keys and values.
     *
     * Eg:
     * ```json
     * [
     *   {
     *     "id": "id-1-value",
     *     "name": "name-1-value"
     *   },
     *   {
     *     "id": "id-2-value",
     *     "name": "name-2-value"
     *   }
     * ]
     *```
     * This will result in 2 HTTP requests to external.
     * 1. With method GET and format = `https://external.api/v1/{id}?name={name}`
     *     * `https://external.api/v1/id-1?name=name-1-value`
     *     * `https://external.api/v1/id-2?name=name-2-value`
     *
     * 2. With method POSt and format = `https://external.api/v1`
     *     * `https://external.api/v1` body:  `{"id": "id-1-value","name": "name-1-value"}`
     *     * `https://external.api/v1` body:  `{"id": "id-2-value","name": "name-2-value"}`
     *
     * Requires preflight to be registered
     */
    MULTIPLE_FIRE_PREFLIGHT
}

data class RequestSetting(
    val method: String,
    val url: String,
)

data class PreFlight(
    val url: String,
)