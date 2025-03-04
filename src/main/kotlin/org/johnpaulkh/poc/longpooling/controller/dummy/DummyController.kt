package org.johnpaulkh.poc.longpooling.controller.dummy

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

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
    fun multiplePreflights() = listOf(
        mapOf("pink" to "floyd"),
        mapOf("dream" to "theater"),
    )

    @PostMapping("/multiple-with-preflights")
    fun multipleWithPreflights(
        @RequestBody body: String
    ) = body


    @PostMapping("/post-it")
    fun postIt(@RequestBody request: Map<String, String>) {
        logger.debug("request = {}", request)
    }
}