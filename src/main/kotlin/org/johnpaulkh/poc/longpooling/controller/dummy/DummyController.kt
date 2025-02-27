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

    @PostMapping("/post-it")
    fun postIt(@RequestBody request: Map<String, String>) {
        logger.debug("request = {}", request)
    }
}