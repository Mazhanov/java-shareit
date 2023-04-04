package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@AllArgsConstructor
@Validated
public class ItemRequestController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) long requestorId,
                                         @Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Добавлен новый запрос {}", requestDto);
        return itemRequestClient.create(requestorId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwn(@RequestHeader(USER_ID_HEADER) long requestorId) {
        log.info("Возвращен список всех запросов пользователя {}", requestorId);
        return itemRequestClient.getAllOwn(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestOtherUsers(@RequestHeader(USER_ID_HEADER) long requestorId,
                                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                        @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Возвращен список всех запросов для пользователя {}", requestorId);
        return itemRequestClient.getAllRequestOtherUsers(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader(USER_ID_HEADER) long requestorId,
                                  @PathVariable(name = "requestId") long requestId) {
        log.info("Возвращен запрос для пользователя {}", requestorId);
        return itemRequestClient.getById(requestorId, requestId);
    }
}
