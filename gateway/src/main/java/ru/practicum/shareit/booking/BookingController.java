package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;


    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody CreateBookingDto dto,
                                                @RequestHeader(USER_ID_HEADER) long userId) {

        boolean startBeforeNow = dto.getStart().isBefore(LocalDateTime.now());
        boolean endBeforeNow = dto.getEnd().isBefore(LocalDateTime.now());
        boolean startAfterEnd = dto.getStart().isAfter(dto.getEnd());
        boolean startEqualEnd = dto.getStart().equals(dto.getEnd());

        if (startBeforeNow || endBeforeNow || startAfterEnd || startEqualEnd) {
            throw new ValidationException("Ошибка валидации даты старта или финиша");
        }
        log.info("Добавлено новое бронирование {}, userId = {}", dto, userId);
        return bookingClient.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeStatusBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                                      @PathVariable long bookingId,
                                                      @RequestParam(name = "approved") boolean status) {
        log.info("Обновлен статус бронирования booking {}, status {}", bookingId, status);
        return bookingClient.changeStatusBooking(userId, bookingId, status);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEADER) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBooker(@RequestHeader(USER_ID_HEADER) long userId,
                                                 @RequestParam(name = "state",defaultValue = "ALL") String stateParam,
                                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader(USER_ID_HEADER) long userId,
                                                @RequestParam(name = "state",defaultValue = "ALL") String stateParam,
                                                @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getAllByOwner(userId, state, from, size);
    }
}
