package com.example.apilogin.controller;

import com.example.apilogin.domain.dto.LoginDTO;
import com.example.apilogin.domain.dto.UserDTO;
import com.example.apilogin.domain.model.Response;
import com.example.apilogin.domain.redis.Token;
import com.example.apilogin.exception.IncorrectCredentialsException;
import com.example.apilogin.exception.NotAuthorizedException;
import com.example.apilogin.service.AuthService;
import com.example.apilogin.service.ConfirmService;
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
    final ConfirmService confirmation;

    @GetMapping("/profile")
    @ResponseStatus(OK)
    public ResponseEntity<Response<Object>> profile(@RequestHeader("token") UUID token)
            throws NotAuthorizedException, IncorrectCredentialsException {
        Token auth = authService.authenticate(token);
        return ResponseEntity.ok().body(Response.builder()
                .date(now())
                .status(OK).statusCode(OK.value())
                .message("Showing user")
                .data(userService.toView(auth.getId()))
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

    @GetMapping("/confirm")
    public ResponseEntity<Response<Object>> confirm(@RequestParam("token") UUID token)
            throws NotAuthorizedException, IncorrectCredentialsException, MessagingException {
        confirmation.confirmation(token);
        return ResponseEntity.ok().body(
                Response.builder()
                        .date(now())
                        .status(OK).statusCode(OK.value())
                        .message("Successful confirmation")
                        .build()
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response<Object>> delete(@RequestParam("token") UUID token)
            throws NotAuthorizedException, IncorrectCredentialsException {
        Token userToken = authService.authenticate(token);
        userService.delete(userToken.getId());
        return ResponseEntity.ok().body(
                Response.builder()
                        .date(now())
                        .status(OK).statusCode(OK.value())
                        .message("Successfully deleted")
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<Response<Object>> update(@RequestBody UserDTO userDTO, @RequestParam("token") UUID token)
            throws NotAuthorizedException, IncorrectCredentialsException {
        Token userToken = authService.authenticate(token);
        userService.update(userDTO, userToken.getId());
        return ResponseEntity.ok().body(
                Response.builder()
                        .date(now())
                        .status(OK).statusCode(OK.value())
                        .message("Successfully updated")
                        .build()
        );
    }
}