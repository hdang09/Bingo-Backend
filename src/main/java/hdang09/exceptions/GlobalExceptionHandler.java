package hdang09.exceptions;

import hdang09.enums.ResponseStatus;
import hdang09.models.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Exception>> handleException(Exception ex) {
        Response<Exception> response = new Response<>(ResponseStatus.ERROR, ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidateException(BindException ex) {
        // Get all errors
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Response<Map<String, String>> response = new Response<>(ResponseStatus.ERROR, "Validate failed!", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
