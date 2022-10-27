package cl.wom.batch.config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import cl.wom.batch.step.one.StepOneReader;
import cl.wom.batch.step.one.to.StepOneDataTO;
import lombok.AllArgsConstructor;

@SpringBatchTest
@EnableAutoConfiguration
@AllArgsConstructor
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
public class BatchApplicationTests {

	private JobLauncherTestUtils jobLauncherTestUtils;

	private JobRepositoryTestUtils jobRepositoryTestUtils;

	private StepOneReader stepOneReader;

	@After
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	private JobParameters defaultJobParameters() {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		// paramsBuilder.addString("file.input", TEST_INPUT);
		return paramsBuilder.toJobParameters();
	}

	@Test
	public void completedTest() throws Exception {

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
		JobInstance actualJobInstance = jobExecution.getJobInstance();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertEquals(actualJobInstance.getJobName(), is("Job"));
		assertEquals(actualJobExitStatus.getExitCode(), is("COMPLETED"));

	}

	@Test
	public void givenStepZeroExecuted_thenSuccess() throws Exception {

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("stepZero", defaultJobParameters());
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertEquals(actualStepExecutions.size(), is(1));
		assertEquals(actualJobExitStatus.getExitCode(), is("COMPLETED"));

	}

	@Test
	public void givenStepOneExecuted_thenSuccess() {

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("stepOne", defaultJobParameters());
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualExitStatus = jobExecution.getExitStatus();

		// then
		assertEquals(actualStepExecutions.size(), is(1));
		assertEquals(actualExitStatus.getExitCode(), is("COMPLETED"));
		actualStepExecutions.forEach(stepExecution -> {
			assertEquals(stepExecution.getWriteCount(), is(8));
		});

	}

	@Test
	public void givenMockedStep_whenReaderCalled_thenSuccess() throws Exception {

		// given
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

		// when
		StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			ItemReader<? extends StepOneDataTO> itemReader = stepOneReader.reader();

			StepOneDataTO stepOneDataTO;
			while ((stepOneDataTO = itemReader.read()) != null) {
				assertEquals(stepOneDataTO.getStatus(), is("OK"));
			}

			return null;
		});

	}
}
