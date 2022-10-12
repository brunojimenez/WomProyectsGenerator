package cl.wom.batch.step.one;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StepOneWriter implements ItemWriter<String> {

	@Override
	public void write(List<? extends String> items) throws Exception {
		items.forEach(i -> log.info("[write] i=" + i));
	}

}
