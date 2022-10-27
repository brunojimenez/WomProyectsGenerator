package cl.wom.batch.lintener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomJobExecutionListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("[beforeJob] getJobName={}", jobExecution.getJobInstance().getJobName());
		jobExecution.getExecutionContext().put("key", "Hello Key");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("[afterJob] key={}", jobExecution.getExecutionContext().get("key"));
	}

}
