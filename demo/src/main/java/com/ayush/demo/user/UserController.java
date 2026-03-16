package com.ayush.demo.user;

import com.ayush.demo.Dto.UserRequestDto;
import com.ayush.demo.Dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management",description = "APIs for managing user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping

    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) throws AccessDeniedException {
        userService.deleteUserById(id);
    }

    @Operation(summary = "get user by id",description = "return a single user by id")
    @ApiResponse(responseCode = "200",description = "user found")
    @ApiResponse(responseCode = "404",description = "user not found")
    @GetMapping("/{id}")
    public UserResponseDto getUserById( @Parameter(description = "Id of user to retrieve") @PathVariable Long id) throws AccessDeniedException {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,@RequestBody UserRequestDto updatedUser) throws AccessDeniedException {
        return userService.updateUser(id,updatedUser);

    }

    @GetMapping("/age/{age}")
    public Page<UserResponseDto> getUserByAge(@PathVariable int age,Pageable pageable){
        return userService.findUserByAge(age,pageable);
    }

    @PostMapping("/{id}/profile-picture")
    public String uploadProfilePicture(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        return userService.uploadUserProfilePicture(id,file);
    }

}
