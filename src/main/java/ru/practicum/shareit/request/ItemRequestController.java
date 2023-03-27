package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.PageableCreate;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@AllArgsConstructor
@Validated
public class ItemRequestController {
    private RequestService requestService;
    private static final String USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto create(@RequestHeader(name = USER_ID) long requestorId,
                                 @Valid @RequestBody ItemRequestDto requestDto) {
        ItemRequestDto newRequest = requestService.create(requestDto, requestorId, LocalDateTime.now());
        log.info("Добавлен новый запрос {}", newRequest);
        return newRequest;
    }

    @GetMapping
    public List<ItemRequestDto> getAllOwn(@RequestHeader(name = USER_ID) long requestorId) {
        List<ItemRequestDto> requestsOwn = requestService.getAllOwn(requestorId);
        log.info("Возвращен список всех запросов пользователя {}: {}", requestorId, requestsOwn);
        return requestsOwn;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestOtherUsers(@RequestHeader(name = USER_ID) long requestorId,
                                                        @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                                        @RequestParam(name = "size", defaultValue = "10") @Min(0) int size) {
        List<ItemRequestDto> requestsDto =
                requestService.getAllRequestOtherUsers(requestorId, PageableCreate.pageableCreate(from, size));
        log.info("Возвращен список всех запросов для пользователя {}: {}", requestorId, requestsDto);
        return requestsDto;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader(name = USER_ID) long requestorId,
                                  @PathVariable(name = "requestId") long requestId) {
        ItemRequestDto requestDto = requestService.getById(requestId, requestorId);
        log.info("Возвращен запрос для пользователя {}: {}", requestorId, requestDto);
        return requestDto;
    }
}
