package cl.wom.batch.kafka.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

	private String data;

}
