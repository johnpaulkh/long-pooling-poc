package org.johnpaulkh.poc.longpooling.controller.dummy

import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/dummy")
class DummyController {

    private val logger = LoggerFactory.getLogger(DummyController::class.java)

    @GetMapping("/single-fire")
    fun singleFire() = mapOf(
        "tribunal" to "nganu",
        "tempo" to "das ist",
        "gatra" to "de'remma",
    )

    @GetMapping("/preflight")
    fun preflight() = "test"

    @PostMapping("/single-with-preflight")
    fun singleWithPreflight(
        @RequestBody body: String
    ) = mapOf(
        "pink" to body,
    )

    @GetMapping("/multiple-preflights")
    fun multiplePreflights(): List<Pair<String, String>> {
        val result = ArrayList<Pair<String, String>>()
        for (i in 1..100) {
            val random = UUID.randomUUID()
            result.add(Pair("item-$i", "value-$random"))
        }
        return result
    }

    @PostMapping("/multiple-with-preflights")
    fun multipleWithPreflights(
        @RequestBody body: String
    ) = body


    @PostMapping("/post-it")
    suspend fun postIt(@RequestBody request: Map<String, String>) {
        delay(100)
        logger.debug("request = {}", request)
    }
}