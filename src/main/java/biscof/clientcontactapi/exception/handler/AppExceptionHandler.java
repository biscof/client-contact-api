package biscof.clientcontactapi.exception.handler;

import biscof.clientcontactapi.exception.exceptions.ClientNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import biscof.clientcontactapi.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ErrorResponse> errorList = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new ErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        error.getDefaultMessage(),
                        request.getRequestURI())
                )
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorList);
    }

}
