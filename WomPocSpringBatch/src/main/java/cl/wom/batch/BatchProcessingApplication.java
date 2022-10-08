package cl.wom.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.retry.annotation.EnableRetry;

@PropertySource("file:${APP_ENV}")
@SpringBootApplication
@EnableBatchProcessing
@EnableRetry
public class BatchProcessingApplication {

  public static void main(String[] args) {
    ApplicationContext context = SpringApplication.run(BatchProcessingApplication.class, args);
    SpringApplication.exit(context);
  }
}
