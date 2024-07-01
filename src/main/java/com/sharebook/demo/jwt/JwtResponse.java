package com.sharebook.demo.jwt;

public class JwtResponse {
    private String username;

    public JwtResponse(String username) {
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
