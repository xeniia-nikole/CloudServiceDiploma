package com.diploma.controller;

import com.diploma.dto.JwtRequest;
import com.diploma.dto.ResponseError;
import com.diploma.service.auth.JwtAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class JwtAuthenticationController {
    private static final String LOGIN = "/login";
    private static final String LOGOUT = "/logout";

    private final JwtAuthService jwtAuthService;

    @Autowired
    public JwtAuthenticationController(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }

    @PostMapping(value = LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtRequest> login(@Valid @RequestBody JwtRequest authorization) {
        String authToken = jwtAuthService.login(authorization);
        return authToken != null ? new ResponseEntity<>(new JwtRequest(authToken), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = LOGOUT)
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        jwtAuthService.logout(authToken);
        return HttpStatus.OK;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseError(e.getMessage(), 400));
    }
}
