package com.ayush.demo.auth;

import com.ayush.demo.Dto.LoginReqDto;
import com.ayush.demo.Dto.UserRequestDto;
import com.ayush.demo.Dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserResponseDto RegisterUser(@Valid @RequestBody UserRequestDto userRequestDto){
        return authService.register(userRequestDto);
    }

    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody LoginReqDto loginReqDto){
        return authService.userLogin(loginReqDto);
    }
}
