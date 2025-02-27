package org.johnpaulkh.poc.longpooling.repository

import org.johnpaulkh.poc.longpooling.entity.Job
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface JobRepository : MongoRepository<Job, String> {

    fun existsByClientIdAndName(
        clientId: String,
        name: String,
    ): Boolean

    fun findByClientIdAndName(
        clientId: String,
        name: String,
    ): Job?
}