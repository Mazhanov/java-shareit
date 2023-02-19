package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    public User create(@Valid @RequestBody UserCreateDto dto) {
        User user = userService.create(dto);
        log.info("Создан пользователь {}", user);
        return user;
    }

    @PatchMapping("/{id}")
    public User update(@Valid @RequestBody UserUpdateDto dto, @PathVariable long id) {
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
