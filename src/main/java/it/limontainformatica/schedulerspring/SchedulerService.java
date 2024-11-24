package it.limontainformatica.schedulerspring;

import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class SchedulerService {

    private final Scheduler scheduler;
    public static final Date      bootstrapTime = Date.from(Instant.now());

    public SchedulerService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public String registerJob(JobDefinition jobDefinition) {
        try {
            if (!CronExpression.isValidExpression(jobDefinition.cronExpression)) {
                return "Invalid cron expression: " + jobDefinition.cronExpression;
            }
            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("customMessage", jobDefinition.customMessage);
            JobDetail jobDetail = JobBuilder.newJob(WorkflowJob.class)
                    .withIdentity(jobDefinition.jobName)
                    .usingJobData(jobDataMap)
                    .storeDurably()
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                    .withIdentity(jobDefinition.jobName + "Trigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule(jobDefinition.cronExpression).withMisfireHandlingInstructionIgnoreMisfires())
                    .build();
            if (!scheduler.checkExists(jobDetail.getKey())) {
                scheduler.addJob(jobDetail, true);
            }
            scheduler.scheduleJob(trigger);

            return "Cron job scheduled: " + jobDefinition.jobName + " with expression " + jobDefinition.cronExpression;
        } catch (SchedulerException e) {
            return "Error scheduling job: " + e.getMessage();
        }
    }


    public String removeJob(String jobName) {
        try {
            // Verifica se il job esiste
            JobKey jobKey = new JobKey(jobName);
            if (!scheduler.checkExists(jobKey)) {
                return "Job non trovato: " + jobName;
            }

            // Rimuovi il job (e il trigger associato)
            scheduler.deleteJob(jobKey);

            return "Job " + jobName + " arrestato con successo!";
        } catch (SchedulerException e) {
            e.printStackTrace();
            return "Errore durante l'arresto del job: " + e.getMessage();
        }
    }
}
