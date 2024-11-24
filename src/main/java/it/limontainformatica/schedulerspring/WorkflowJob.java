package it.limontainformatica.schedulerspring;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.Instant;
import java.util.Date;

@Slf4j(topic = "WorkflowJob")
public class WorkflowJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Evito il processing di lavori scaduti.
        if(!context.getScheduledFireTime().before(SchedulerService.bootstrapTime)) {
            String jobName = context.getJobDetail().getKey().getName();
            String customMessage = context.getJobDetail().getJobDataMap().getString("customMessage");
            log.info("Executing workflow job : {} with {} : {} ", jobName, customMessage, context.getScheduledFireTime() + " Date corrente " + Date.from(Instant.now()));
        }
    }
}