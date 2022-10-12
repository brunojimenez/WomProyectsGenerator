package cl.wom.api.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiExceptionModel> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return handleError("SXXXV001", e.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiExceptionModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

		List<Object> subErrors = e.getFieldErrors().stream().map(ApiExceptionModel.FieldSubErrorModel::new)
				.collect(Collectors.toList());

		return handleError("SXXXV002", "Invalid argument", HttpStatus.BAD_REQUEST, subErrors);
	}

	// Agregar errores personalizados así
	/*
	 * @ExceptionHandler(SRV40288ComponentException.class) public
	 * ResponseEntity<ErrorModel> handleSRV40288ComponentException(
	 * SRV40288ComponentException e) { ErrorModel model = new ErrorModel("S278U001",
	 * e.getMessage(), e.getDto());
	 * 
	 * return new ResponseEntity<>(model, HttpStatus.CONFLICT); }
	 */

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiExceptionModel> handleBusinessExceptions(BusinessException e) {
		return handleError(e.getCode(), e.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<ApiExceptionModel> handleException(Throwable e) {
		log.error("Unhandled exception", e);
		return handleError("SXXXC001", "Unhandled exception", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ApiExceptionModel> handleError(String code, String message, HttpStatus status) {
		return handleError(code, message, status, null);
	}

	private ResponseEntity<ApiExceptionModel> handleError(String code, String message, HttpStatus status,
			List<Object> subErrors) {
		ApiExceptionModel model = new ApiExceptionModel(code, message, subErrors);
		return new ResponseEntity<>(model, status);
	}

}
