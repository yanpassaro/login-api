package com.example.apilogin.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Email {
    private String destiny;
    private String subject;
    private String content;
}
