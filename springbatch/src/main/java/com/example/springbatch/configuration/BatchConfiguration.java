package com.example.springbatch.configuration;

import com.example.springbatch.model.Csv;
import com.example.springbatch.writer.ConsoleItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.springbatch.listener.StepExecutionListener;
import org.springframework.core.io.FileSystemResource;

import java.nio.file.FileSystem;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobExecutionListener jobExecutionListener;

    @Autowired
    private StepExecutionListener stepExecutionListener;

    public Step step1() {
        return steps.get("step1")
                .listener(stepExecutionListener)
                .tasklet(helloworldTaklet())
                .build();
    }



    private Tasklet helloworldTaklet() {
        return (new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Hello Spring batch");
                return RepeatStatus.FINISHED;
            }
        });
    }

    @Bean
    public Job helloWorldJob() {
        return jobs.get("helloworldJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(step1())
               // .listener(jobExecutionListener)
                //.next(step2())

                .build();
    }

    @Bean
    public FlatFileItemReader flatFileReader () {
        System.out.println("flatFileReader ...");
        FlatFileItemReader reader = new FlatFileItemReader();
        FileSystemResource fsr = new FileSystemResource("C:\\data2\\test.csv");
        reader.setResource(fsr);
        reader.setLinesToSkip(1);

        reader.setLineMapper(new DefaultLineMapper() {
            {

                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer(";") {
                    {
                        setNames(new String[] { "Id", "Nombre"});
                    }
                });
                //Set values in Csv class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Csv>() {
                    {
                        setTargetType(Csv.class);
                    }
                });
            }
        });
        return reader;
    }


    @Bean
    public ConsoleItemWriter<Csv> writer() {
        return new ConsoleItemWriter<Csv>();
    }

    public Step step2() {
        return steps.get("step2")
                .<Csv,Csv>chunk(3)
                .reader(flatFileReader())
                .writer(writer())
                .build();
    }
}
