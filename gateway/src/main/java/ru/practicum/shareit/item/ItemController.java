package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated({Create.class}) @RequestBody ItemDto dto,
                                         @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Добавлена новая вещь {}", dto);
        return itemClient.create(dto, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated({Update.class}) @RequestBody ItemDto dto,
                       @RequestHeader(USER_ID_HEADER) long userId,
                       @PathVariable long id) {
        log.info("Обновлена вещь {}", dto);
        return itemClient.update(dto, userId, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id, @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Возвращена вещь id {}", id);
        return itemClient.getById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                     @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Возвращен список вещей пользователя id={}", userId);
        return itemClient.getByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Возвращен список вещей по запросу {}", text);
        return itemClient.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(USER_ID_HEADER) long userId, @PathVariable long itemId,
                                    @RequestBody CommentDto commentDto) {
        log.info("Добавлен коммент к вещи {}, {}", itemId, commentDto);
        return itemClient.createComment(userId, itemId, commentDto);
    }
}