package cl.wom.batch.config;

import javax.annotation.Resource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import cl.wom.wompay.to.BillingCycleTO;

@Component
public class BatchConfig extends JobExecutionListenerSupport {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	protected Job job() {
		return jobBuilderFactory //
				.get("job") //
				.incrementer(new RunIdIncrementer()) //
				.listener(this) //
				.start( //
						step()) //
				.build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step") //
				.<String, Boolean>chunk(100) //
				.listener(this) //
				.reader(reader()) //
				.processor(processor()) //
				.writer(writer()) //
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<String> reader() {
		FileSystemResource resource = new FileSystemResource("resources/data.csv");
		FlatFileItemReader<String> reader = new FlatFileItemReader<>();
		reader.setResource((Resource) resource.get(RESOURCE));
		return reader;
	}

}
