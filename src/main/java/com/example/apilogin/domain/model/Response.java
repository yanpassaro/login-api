package com.example.apilogin.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@Builder
public class Response<R> {
    private LocalDate date;
    private Integer statusCode;
    private HttpStatus status;
    private String message;
    private String devMessage;
    private R data;
}
