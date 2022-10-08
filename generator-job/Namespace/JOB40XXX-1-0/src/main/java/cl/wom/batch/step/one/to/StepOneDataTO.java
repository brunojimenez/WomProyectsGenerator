package cl.wom.batch.step.one.to;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class StepOneDataTO {

	@Id
	private String id;
	private String data;
	private String status;

}
