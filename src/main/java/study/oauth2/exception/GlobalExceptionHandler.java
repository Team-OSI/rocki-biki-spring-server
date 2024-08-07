package study.oauth2.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

import study.oauth2.exception.Error.EmailAlreadyExistsException;
import study.oauth2.exception.Error.S3Exception;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		return handleException(ex.getBindingResult());
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
		return handleException(ex, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		return handleException(ex, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
		return handleException(ex, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(S3Exception.class)
	public ResponseEntity<String> handleS3Exception(S3Exception ex) {
		return handleException(ex, HttpStatus.BAD_REQUEST);
	}

	private ResponseEntity<String> handleException(BindingResult bindingResult) {
		List<String> errorMessages = bindingResult.getFieldErrors().stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.toList());
		String errorMessage = String.join(", ", errorMessages);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	}

	private ResponseEntity<String> handleException(Exception ex, HttpStatus status) {
		return ResponseEntity.status(status).body(ex.getMessage());
	}
}
