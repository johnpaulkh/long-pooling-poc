package org.johnpaulkh.poc.longpooling.service

import org.johnpaulkh.poc.longpooling.entity.Job

interface ExecutionService {

    fun execute(job: Job)
}