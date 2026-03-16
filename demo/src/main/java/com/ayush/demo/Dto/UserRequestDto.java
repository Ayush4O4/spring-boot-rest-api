package com.ayush.demo.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @Schema(description = "Name of the User", example = "Ayush")
    @NotBlank(message = "name can't be empty")
    private String name;

    @Schema(description = "Age of the User", example = "19")
    @Min(value = 0, message = "age can't be less than 0")
    private int age;

    @Schema(description = "password", example = "password123")
    @NotBlank(message = "password can't be blank")
    private String password;


}