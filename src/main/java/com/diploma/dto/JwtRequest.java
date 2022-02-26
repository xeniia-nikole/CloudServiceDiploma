package com.diploma.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    @JsonProperty("auth-token")
    private String authToken;

    private String login;
    private String password;

    public JwtRequest(String authToken) {
        this.authToken = authToken;
    }

    public JwtRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
