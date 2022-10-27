package cl.wom.batch.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import cl.wom.batch.lintener.CustomJobExecutionListener;
import cl.wom.batch.lintener.CustomStepExecutionListener;

@RunWith(SpringRunner.class)
@SpringBatchTest
@ContextConfiguration(classes = BatchConfiguration.class)
public class BatchTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	@Mock
	private CustomJobExecutionListener jobExecutionListener;

	@Mock
	private CustomStepExecutionListener stepExecutionListener;

	@Before
	public void clearJobExecutions() {
		this.jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	public void testMyJob() throws Exception {
		// given
		JobParameters jobParameters = this.jobLauncherTestUtils.getUniqueJobParameters();

		// when
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);

		// then
		Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

}
