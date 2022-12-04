package com.example.apilogin.exception;

import com.example.apilogin.domain.model.Response;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.time.LocalDate.now;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class GlobalHandler {

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public ResponseEntity<Response<Object>> handleIncorrectCredentialsException(IncorrectCredentialsException ex) {
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .message(ex.getMessage())
                        .date(now())
                        .statusCode(BAD_REQUEST.value())
                        .status(BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(value = NotAuthorizedException.class)
    public ResponseEntity<Response<Object>> handleNotAuthorizedException(NotAuthorizedException ex) {
        return ResponseEntity.badRequest().body(
                Response.builder()
                        .message(ex.getMessage())
                        .date(now())
                        .statusCode(BAD_REQUEST.value())
                        .status(BAD_REQUEST)
                        .build()
        );
    }
}
