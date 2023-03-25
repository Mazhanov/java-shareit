package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(groups = {Create.class},message = "name cannot be missing")
    @NotNull(groups = {Create.class},message = "name cannot be missing")
    private String name;

    @Email(groups = {Create.class, Update.class}, message = "email is incorrect")
    @NotBlank(groups = {Create.class},message = "email cannot be missing")
    @NotNull(groups = {Create.class},message = "email cannot be missing")
    private String email;
}
