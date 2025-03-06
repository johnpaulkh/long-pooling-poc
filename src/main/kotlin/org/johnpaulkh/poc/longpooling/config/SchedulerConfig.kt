package org.johnpaulkh.poc.longpooling.config

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.johnpaulkh.poc.longpooling.entity.Job
import org.johnpaulkh.poc.longpooling.repository.JobRepository
import org.johnpaulkh.poc.longpooling.service.execution.ExecutionCoordinatorService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.CronTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar

@Configuration
class SchedulerConfig(
    private val taskScheduler: TaskScheduler,
    private val jobRepository: JobRepository,
    private val executionCoordinator: ExecutionCoordinatorService,
) : SchedulingConfigurer {

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler)

        val jobs = jobRepository.findAllByCronIsNotNull()
        jobs.forEach { job ->
            val cron = job.cron!!
            taskRegistrar.addCronTask(CronTask(launch(job), cron))
        }
    }

    private fun launch(job: Job): Runnable = Runnable {
        runBlocking { launch { executionCoordinator.execute(job) } }
    }
}