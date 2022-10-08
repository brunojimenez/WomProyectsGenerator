package com.example.springbatch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Component;

@Component
public class JobExecutionListener implements org.springframework.batch.core.JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("beforeJob Listener");
        System.out.println("getJobName[" + jobExecution.getJobInstance().getJobName() + "]");
        jobExecution.getExecutionContext().put("key", "hola jimmy");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("afterJob Listener");
        String key = (String) jobExecution.getExecutionContext().get("key");
        System.out.println("key[" + key + "]");

    }
}
