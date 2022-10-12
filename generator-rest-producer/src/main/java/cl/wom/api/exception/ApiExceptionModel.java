package cl.wom.api.exception;

import java.util.Collections;
import java.util.List;

import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ApiExceptionModel {

	private final String code;
	private final String message;

	@JsonInclude(Include.NON_NULL)
	private final List<Object> subErrors;

	public ApiExceptionModel(String code, String reason) {
		this(code, reason, null);
	}

	public ApiExceptionModel(String code, String reason, Object subError) {
		this(code, reason, Collections.singletonList(subError));
	}

	public ApiExceptionModel(String code, String message, List<Object> subErrors) {
		this.code = code;
		this.message = message;
		this.subErrors = subErrors;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public List<Object> getSubErrors() {
		return subErrors;
	}

	public static class FieldSubErrorModel {

		private final String field;
		private final Object value;
		private final String type;

		public FieldSubErrorModel(FieldError error) {
			field = String.join(".", error.getObjectName(), error.getField());
			value = error.getRejectedValue();
			type = error.getDefaultMessage();
		}

		public String getField() {
			return field;
		}

		public Object getValue() {
			return value;
		}

		public String getType() {
			return type;
		}

	}

}
