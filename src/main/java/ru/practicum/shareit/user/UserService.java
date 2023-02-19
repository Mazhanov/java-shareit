package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    User getById(long id);

    List<User> getAll();

    User create(UserCreateDto dto);

    User update(UserUpdateDto dto, long id);

    void remove(long id);
}
