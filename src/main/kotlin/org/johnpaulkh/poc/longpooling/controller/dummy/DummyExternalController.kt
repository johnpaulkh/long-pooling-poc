package org.johnpaulkh.poc.longpooling.controller.dummy

import io.swagger.v3.oas.annotations.Hidden
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.random.Random

@RestController
@RequestMapping("/dummy/external")
@Hidden
class DummyExternalController {

    @GetMapping("/no-preflight")
    fun postNoPreflight(): List<ExternalResult> = generateExternalResults()

    @PostMapping("/with-request-body")
    fun postWithRequestBody(
        @RequestBody request: InternalPreflightResult
    ) = generateExternalResults()
        .filter { it.value in request.from .. request.to }

    @GetMapping("/with-param")
    fun getWithParam(
        @RequestParam from: Long,
        @RequestParam to: Long,
    ): List<ExternalResult> = generateExternalResults()
        .filter { it.value in from..to }

    @GetMapping
    fun getWithId(@RequestParam id: String) =
        ExternalResult(id, "item-$id", "value-$id", Random.nextLong(100))

    @PostMapping
    fun postWithId(@RequestBody idRequest: InternalIdResult) =
        ExternalResult(
            id = idRequest.id,
            name ="item-${idRequest.id}",
            status = "value-${idRequest.id}",
            value = Random.nextLong(100)
        )

    private fun generateExternalResults(): List<ExternalResult> {
        val result = ArrayList<ExternalResult>()
        for (i in 1..10) {
            val id = UUID.randomUUID().toString()
            val name = "name-$id"
            val status = "status-$i"
            val value = Random.nextLong(100)

            result.add(ExternalResult(id, name, status, value))
        }
        return result
    }
}

data class ExternalResult(
    val id: String,
    val name: String,
    val status: String,
    val value: Long,
)