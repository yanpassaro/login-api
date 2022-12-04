package com.example.apilogin.controller;

import com.example.apilogin.domain.dto.LoginDTO;
import com.example.apilogin.domain.dto.UserDTO;
import com.example.apilogin.domain.redis.Token;
import com.example.apilogin.domain.model.Response;
import com.example.apilogin.exception.IncorrectCredentialsException;
import com.example.apilogin.exception.NotAuthorizedException;
import com.example.apilogin.service.AuthService;
import com.example.apilogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.UUID;

import static java.time.LocalDate.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    final UserService userService;
    final AuthService authService;


    @GetMapping("/profile")
    @ResponseStatus(OK)
    public ResponseEntity<Response<Object>> profile(@RequestHeader("token") UUID token)
            throws NotAuthorizedException {
        Token auth = authService.authenticate(token);
        return ResponseEntity.ok().body(Response.builder()
                .date(now())
                .status(OK).statusCode(OK.value())
                .message("Showing user")
                .build());
    }

    @PostMapping("/register")
    @ResponseStatus(OK)
    public ResponseEntity<Response<Object>> register(@RequestBody @Valid UserDTO userDTO)
            throws IncorrectCredentialsException, MessagingException {
        userService.register(userDTO);
        return ResponseEntity.ok().body(Response.builder()
                .date(now())
                .status(OK).statusCode(OK.value())
                .message("Registered user")
                .build());
    }


    @PostMapping ("/login")
    public ResponseEntity<Response<Object>> login(@RequestBody @Valid LoginDTO loginDTO)
            throws NotAuthorizedException {
        UUID userToken = authService.login(loginDTO);
        return ResponseEntity.ok().body(
                Response.builder()
                        .date(now())
                        .status(OK).statusCode(OK.value())
                        .data(userToken)
                        .build()
        );
    }

    @GetMapping ("/confirm")
    public ResponseEntity<Response<Object>> confirm(@RequestParam("id") Long id)
            throws NotAuthorizedException {
        authService.confirmation(id);
        return ResponseEntity.ok().body(
                Response.builder()
                        .date(now())
                        .status(OK).statusCode(OK.value())
                        .message("Successful confirmation")
                        .build()
        );
    }


}