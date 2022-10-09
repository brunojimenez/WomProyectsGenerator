package cl.wom.consumer.persistence.to;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Person")
public class PersonDTO {

	@Id
	private String id;

	private boolean active;

	// Expire document after time
	@Indexed(expireAfterSeconds = 86400 * 180)
	private Date updateAt;

	@Indexed
	private String rut;

	private String name;

	private String lastname;

	private List<String> friends;

	@Transient
	private String fullName;

	// Getter/Setter of transient (not saved) attribute

	public void setFullName() {
		this.fullName = this.name + "" + this.lastname;
	}

	public String getFullName() {
		return this.fullName;
	}

}
