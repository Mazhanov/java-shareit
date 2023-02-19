package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public class UserMapper {
    public static User toUserCreate(UserCreateDto dto) {
        return new User(dto.getName(), dto.getEmail());
    }

    public static User toUserUpdate(UserUpdateDto dto, long id) {
        return new User(id, dto.getName(), dto.getEmail());
    }
}
