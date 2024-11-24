package it.limontainformatica.schedulerspring;


import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.JobName;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {


    @Autowired
    private SchedulerService schedulerService;

    @PostMapping("/schedule")
    public String scheduleCronJob(@RequestBody JobDefinition jobDefinition)
    {
        return schedulerService.registerJob(jobDefinition);
    }

    @PutMapping("/update")
    public String updateJob(@RequestBody JobDefinition jobDefinition) {
        try {
            schedulerService.removeJob(jobDefinition.jobName);
            return schedulerService.registerJob(jobDefinition);
        } catch (Exception e) {
            e.printStackTrace();
            return "Errore durante l'aggiornamento del job: " + e.getMessage();
        }
    }


    @DeleteMapping("/stop/{jobName}")
    public String stopJob(@PathVariable String jobName) {
        return schedulerService.removeJob(jobName);
    }
}