package org.johnpaulkh.poc.longpooling.controller.dummy

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

@RestController
@RequestMapping("/dummy/internal")
class DummyInternalController {

    private val logger: Logger = LoggerFactory.getLogger(DummyInternalController::class.java)

    @PostMapping("/handle")
    fun handle(
        @RequestBody request: ExternalResult,
    ) {
        logger.debug("Received external result: {}", request)
    }

    @GetMapping("/single-preflight")
    fun singlePreflight(): InternalPreflightResult {
        val from = Random.nextLong(100)
        val to = Random.nextLong(from, 100)
        return InternalPreflightResult(from, to)
    }

    @GetMapping("/multiple-preflights")
    fun multiplePreflights(): List<String> {
        val result = ArrayList<String>()
        for (i in 1..10) {
            result.add(UUID.randomUUID().toString())
        }
        return result
    }
}

data class InternalPreflightResult(
    val from: Long,
    val to: Long,
)