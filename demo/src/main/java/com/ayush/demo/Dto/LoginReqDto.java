package com.ayush.demo.Dto;

import jakarta.validation.constraints.NotBlank;

public class LoginReqDto {

    @NotBlank(message = "name must not be blank")
    private String name;
    @NotBlank(message = "password must not be blank")
    private String password;

    public LoginReqDto(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
