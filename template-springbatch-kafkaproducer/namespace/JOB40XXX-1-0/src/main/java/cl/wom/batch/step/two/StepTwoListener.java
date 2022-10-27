package cl.wom.batch.step.two;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import cl.wom.batch.step.two.to.StepTwoDataTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StepTwoListener implements ItemReadListener<StepTwoDataTO> {

	@Override
	public void beforeRead() {
		log.info("[beforeRead] Begin");
	}

	@Override
	public void afterRead(StepTwoDataTO item) {
		log.info("[afterRead] item={}", item);
	}

	@Override
	public void onReadError(Exception ex) {
		log.info("[onReadError] Error={}", ex);
	}

}
