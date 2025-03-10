package org.johnpaulkh.poc.longpooling.controller.dummy

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.random.Random

@RestController
@RequestMapping("/dummy/internal")
class DummyInternalController(
    private val objectMapper: ObjectMapper,
) {

    private val logger: Logger = LoggerFactory.getLogger(DummyInternalController::class.java)

    @PostMapping("/handle-list")
    fun handle(
        @RequestBody request: List<ExternalResult>,
    ) {
        logger.debug(
            "Received list of external result: {}",
            objectMapper.writeValueAsString(request))
    }

    @PostMapping("/handle-single")
    fun handleSingle(
        @RequestBody request: ExternalResult
    ) {
        logger.debug(
            "Received single external result: {}",
            objectMapper.writeValueAsString(request)
        )
    }

    @GetMapping("/single-preflight")
    fun singlePreflight(): InternalPreflightResult {
        val from = Random.nextLong(100)
        val to = Random.nextLong(from, 100)
        return InternalPreflightResult(from, to)
    }

    @GetMapping("/multiple-preflights")
    fun multiplePreflights(): List<InternalIdResult> {
        val result = ArrayList<InternalIdResult>()
        for (i in 1..10) {
            result.add(InternalIdResult(id = UUID.randomUUID().toString()))
        }
        return result
    }
}

data class InternalPreflightResult(
    val from: Long,
    val to: Long,
)

data class InternalIdResult(
    val id: String,
)