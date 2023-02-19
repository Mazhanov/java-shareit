package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserCreateDto {
    @NotBlank(message = "name cannot be missing")
    private String name;

    @Email(message = "email is incorrect")
    @NotBlank(message = "email cannot be missing")
    private String email;
}
