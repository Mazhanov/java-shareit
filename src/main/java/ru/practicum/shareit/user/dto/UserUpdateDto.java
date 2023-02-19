package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class UserUpdateDto {
    private String name;
    @Email(message = "email is incorrect")
    private String email;
}
