package com.example.springbootecommerceapi.exception;

import com.example.springbootecommerceapi.model.OutOfStockItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String TRACE = "trace";
    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    // handles exceptions thrown by @valid
    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details.");

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    @ExceptionHandler(AccountActivationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleAccountActivationException(
            AccountActivationException exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> handleProductOutOfStock(
            ProductOutOfStockException exception,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CREATED.value(),
                "Some or all products wanted are out of stock"
        );

        for (OutOfStockItemDTO item : exception.getItems()) {
            errorResponse.addOutOfStock(item.getProductName(), item.getStockQuantity(), item.getQuantityWanted());
        }

        return  ResponseEntity.accepted().body(errorResponse);
    }

    @ExceptionHandler(ProductException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> handleProductException(
            ProductException exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.ACCEPTED,
                request
        );
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleInputConstraintViolation(
            ConstraintViolationException exception,
            WebRequest request) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY,
                request
        );
    }

    @ExceptionHandler(PasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlePasswordException(
            PasswordException exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(PasswordTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlePasswordTokenException(
            PasswordTokenException exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }


    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Object> handleUserException(
            UserException exception,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                HttpStatus.ACCEPTED,
                request
        );
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request){
        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }




    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        return buildErrorResponse(exception, exception.getMessage(), status,request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                message
        );

        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(String.valueOf(exception.getStackTrace()));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        System.out.println(value.toString());
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }
}
