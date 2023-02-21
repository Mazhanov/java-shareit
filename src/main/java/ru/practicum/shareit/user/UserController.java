package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
        List<User> users = userService.getAll();
        log.info("Возвращен список пользователей {}", users);
        return users;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) {
        User user = userService.getById(id);
        log.info("Возвращен пользователь {}", user);
        return user;
    }

    @PostMapping
    public User create(@Validated({Create.class}) @RequestBody UserDto dto) {
        User user = userService.create(dto);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @PatchMapping("/{id}")
    public User update(@Validated({Update.class}) @RequestBody UserDto dto, @PathVariable long id) {
        User user = userService.update(dto, id);
        log.info("Обновлен пользователь {}", user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable long id) {
        userService.remove(id);
        log.info("Обновлен пользователь с id={}", id);
    }
}