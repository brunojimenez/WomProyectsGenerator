package cl.wom.batch.step.two;

import org.slf4j.MDC;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import cl.wom.batch.step.two.to.StepTwoDataTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class StepTwoProcessor implements ItemProcessor<StepTwoDataTO, String> {

	@Override
	public String process(StepTwoDataTO data) throws Exception {

		MDC.put("rut", data.getRut());

		log.info("[process] data={}", data);

		return data.getRut();
	}

}
