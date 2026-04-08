package franchise_api.infrastructure.web.handler;

import franchise_api.domain.exception.DomainException;
import franchise_api.domain.exception.NotFoundException;
import franchise_api.infrastructure.web.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse("NOT_FOUND", exception.getMessage(), Instant.now()));
	}

	@ExceptionHandler({DomainException.class, ServerWebInputException.class})
	public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException exception) {
		return ResponseEntity.badRequest()
				.body(new ErrorResponse("BAD_REQUEST", exception.getMessage(), Instant.now()));
	}

	@ExceptionHandler(WebExchangeBindException.class)
	public ResponseEntity<ErrorResponse> handleValidation(WebExchangeBindException exception) {
		String message = exception.getFieldErrors().stream()
				.findFirst()
				.map(error -> error.getDefaultMessage() == null ? error.getField() : error.getDefaultMessage())
				.orElse("Validation error");
		return ResponseEntity.badRequest()
				.body(new ErrorResponse("VALIDATION_ERROR", message, Instant.now()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception exception) {
		LOGGER.error("Unhandled error while processing request", exception);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse("INTERNAL_ERROR", "Unexpected server error", Instant.now()));
	}
}