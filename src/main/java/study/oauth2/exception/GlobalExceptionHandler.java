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

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		return handleException(ex.getBindingResult(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
		return handleException(ex, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException ex) {
		return handleException(ex, HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<String> handleException(BindingResult bindingResult, HttpStatus status) {
		List<String> errorMessages = bindingResult.getFieldErrors().stream()
			.map(FieldError::getDefaultMessage)
			.collect(Collectors.toList());
		String errorMessage = String.join(", ", errorMessages);
		return ResponseEntity.status(status).body(errorMessage);
	}

	private ResponseEntity<String> handleException(Exception ex, HttpStatus status) {
		return ResponseEntity.status(status).body(ex.getMessage());
	}
}
