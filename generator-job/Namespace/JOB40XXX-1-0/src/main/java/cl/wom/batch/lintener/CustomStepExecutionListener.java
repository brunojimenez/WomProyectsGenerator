package cl.wom.batch.lintener;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomStepExecutionListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		log.info("[beforeStep] getStepName={}", stepExecution.getStepName());
		log.info("[beforeStep] getJobParameters={}", stepExecution.getJobExecution().getJobParameters());
		
		// Set trace id to logger
		MDC.put("trace-id", UUID.randomUUID().toString().substring(0, 8));
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("[afterStep]");
		
		// Clear context
		MDC.clear();
		
		return null;
	}

}
