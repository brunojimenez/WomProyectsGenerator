package com.example.springbatch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class StepExecutionListener implements org.springframework.batch.core.StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("beforeStep Listener");
        System.out.println("getStepName[" + stepExecution.getStepName() + "]");
        System.out.println("Los parameters del job[" + stepExecution.getJobExecution().getJobParameters() + "]");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("afterStep Listener");
        return null;
    }
}
