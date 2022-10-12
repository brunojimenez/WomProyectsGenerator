package cl.wom.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.wom.batch.lintener.CustomJobExecutionListener;
import cl.wom.batch.lintener.CustomStepExecutionListener;
import cl.wom.batch.service.BatchService;
import cl.wom.batch.step.one.StepOneProcessor;
import cl.wom.batch.step.one.StepOneReader;
import cl.wom.batch.step.one.StepOneWriter;
import cl.wom.batch.step.one.to.StepOneDataTO;
import cl.wom.batch.step.two.StepTwoListener;
import cl.wom.batch.step.two.StepTwoProcessor;
import cl.wom.batch.step.two.StepTwoReader;
import cl.wom.batch.step.two.StepTwoWriter;
import cl.wom.batch.step.two.to.StepTwoDataTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@Configuration
@AllArgsConstructor
// extends JobExecutionListenerSupport
public class BatchConfiguration {

	private final JobBuilderFactory jobs;

	private final StepBuilderFactory steps;

	private final CustomJobExecutionListener jobExecutionListener;

	private final CustomStepExecutionListener stepExecutionListener;

	private final BatchService service;

	// Step One
	private final StepOneProcessor stepOneProcessor;
	private final StepOneReader stepOneReader;
	private final StepOneWriter stepOneWriter;

	// Step Two
	private final StepTwoListener stepTwoListener;
	private final StepTwoReader stepTwoReader;
	private final StepTwoProcessor stepTwoProcessor;
	private final StepTwoWriter stepTwoWriter;

	@Bean
	Job job() {
		log.info("[job] Begin");
		return jobs.get("Job") //
				.incrementer(new RunIdIncrementer()) //
				.listener(jobExecutionListener) //
				.start(stepZero()) //
				.next(stepOne()) //
				.next(stepTwo()) //
				.build();
	}

	public Step stepZero() {
		log.info("[stepZero] Begin");
		return steps.get("stepZero") //
				.listener(stepExecutionListener) //
				.tasklet(stepZeroTasklet()) //
				.build();
	}

	// MongoDB reader example
	public Step stepOne() {
		return steps.get("stepOne") //
				.listener(stepExecutionListener) //
				.<StepOneDataTO, String>chunk(2) //
				.reader(stepOneReader.reader()) //
				.processor(stepOneProcessor) //
				.writer(stepOneWriter) //
				.build();
	}

	// File reader example
	public Step stepTwo() {
		return steps.get("stepTwo") //
				.listener(stepExecutionListener) //
				.<StepTwoDataTO, String>chunk(2) //
				.listener(stepTwoListener) // Reader listener
				.reader(stepTwoReader.reader()) //
				.processor(stepTwoProcessor) //
				.writer(stepTwoWriter) //
				.build();
	}

	// Tasklet example
	private Tasklet stepZeroTasklet() {
		return (new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
				log.info("[Tasklet] Begin");

				service.process();

				log.info("[Tasklet] End");
				return RepeatStatus.FINISHED;
			}
		});
	}

}
