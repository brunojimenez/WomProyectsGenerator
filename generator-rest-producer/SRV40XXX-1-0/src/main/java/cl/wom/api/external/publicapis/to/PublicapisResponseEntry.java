package cl.wom.api.external.publicapis.to;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicapisResponseEntry {
	@JsonProperty("API")
	private String api;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Category")
	private String category;
}