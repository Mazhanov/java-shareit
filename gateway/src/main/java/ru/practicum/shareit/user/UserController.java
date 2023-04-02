package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Возвращен список пользователей");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id) {
        log.info("Возвращен пользователь {}", id);
        return userClient.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody UserDto dto) {
        log.info("Создан пользователь {}", dto);
        return userClient.create(dto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated({Update.class}) @RequestBody UserDto dto, @PathVariable long id) {
        log.info("Обновлен пользователь {}", dto);
        return userClient.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> remove(@PathVariable long id) {
        log.info("Обновлен пользователь с id={}", id);
        return userClient.remove(id);
    }
}