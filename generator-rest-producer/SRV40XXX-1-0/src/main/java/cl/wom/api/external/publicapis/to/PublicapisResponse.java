package cl.wom.api.external.publicapis.to;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PublicapisResponse {
	private Integer count;
	private List<PublicapisResponseEntry> entries;
}