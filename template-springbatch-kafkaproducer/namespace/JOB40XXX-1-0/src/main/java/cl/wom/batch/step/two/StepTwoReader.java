package cl.wom.batch.step.two;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import cl.wom.batch.step.two.to.StepTwoDataTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StepTwoReader {

	@Value("${batch.step.two.file}")
	private String stepTwoFile;

	public ItemReader<StepTwoDataTO> reader() {

		log.info("Begin");

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("rut", "account");

		lineTokenizer.setDelimiter(";");
		lineTokenizer.setStrict(false);

		BeanWrapperFieldSetMapper<StepTwoDataTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(StepTwoDataTO.class);

		DefaultLineMapper<StepTwoDataTO> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(lineTokenizer);
		defaultLineMapper.setFieldSetMapper(fieldSetMapper);

		LineCallbackHandler lineCallbackHandler = line -> {
			String[] header = line.split(";");

			StepTwoDataTO stepTwoDataTO = new StepTwoDataTO();
			stepTwoDataTO.setRut(header[0]);
			stepTwoDataTO.setAccount(header[1]);

			log.info("[reader] Skiped={}", stepTwoDataTO);
		};

		FileSystemResource fsr = new FileSystemResource(stepTwoFile);

		FlatFileItemReader<StepTwoDataTO> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setSkippedLinesCallback(lineCallbackHandler);
		reader.setLineMapper(defaultLineMapper);
		reader.setResource(fsr);

		log.info("[reader] End");

		return reader;
	}

}
