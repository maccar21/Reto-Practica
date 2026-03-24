package com.bancolombia.chocolatinazo.application.dto.response;

public class AuthLoginResponse {

    private String token;
    private UserResponse user;

    public AuthLoginResponse() {
    }

    public AuthLoginResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}

