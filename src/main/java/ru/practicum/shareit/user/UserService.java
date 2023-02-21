package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User getById(long id);

    List<User> getAll();

    User create(UserDto dto);

    User update(UserDto dto, long id);

    void remove(long id);
}
