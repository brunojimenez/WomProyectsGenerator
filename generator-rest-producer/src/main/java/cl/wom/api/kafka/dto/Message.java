package cl.wom.api.kafka.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

	private String data;

}
