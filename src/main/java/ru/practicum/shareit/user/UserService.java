package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(long id);

    List<UserDto> getAll();

    UserDto create(UserDto dto);

    UserDto update(UserDto dto, long id);

    void remove(long id);
}
