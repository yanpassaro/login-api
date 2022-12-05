package com.example.apilogin.domain.dto;

import javax.validation.constraints.*;

import static org.springframework.util.Base64Utils.encodeToString;

public record UserDTO(
                      @Email @NotNull @NotBlank @NotEmpty @Size(min = 8, max = 30) String login,
                      @NotNull @NotBlank @NotEmpty @Size(max = 30) String name,
                      @NotNull @NotBlank @NotEmpty @Size(min = 8, max = 30) String password) {

    @Override
    public String password() {
        return encodeToString(encodeToString(password.getBytes()).getBytes());
    }
}