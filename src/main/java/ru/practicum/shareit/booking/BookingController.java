package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {
    private BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody CreateBookingDto dto,
                                    @RequestHeader(name = "X-Sharer-User-Id") long userId) {
        BookingDto booking = bookingService.createBooking(dto, userId);
        log.info("Добавлено новое бронирование {}", booking);
        return booking;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeStatusBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                          @PathVariable long bookingId,
                                          @RequestParam(name = "approved") boolean status) {
        BookingDto booking = bookingService.changeStatusBooking(userId, bookingId, status);
        log.info("Обновлен статус бронирования {}", booking);
        return booking;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                  @PathVariable long bookingId) {
        BookingDto booking = bookingService.getBooking(userId, bookingId);
        log.info("Получена информация о бронировании: {}", booking);
        return booking;
    }

    @GetMapping
    List<BookingDto> getAllByBooker(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                    @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookingsByBooker = bookingService.getAllByBooker(userId, state);
        log.info("Получен список бронирований пользователя: {}", bookingsByBooker);
        return bookingsByBooker;
    }

    @GetMapping("/owner")
    List<BookingDto> getAllByOwner(@RequestHeader(name = "X-Sharer-User-Id") long userId,
                                    @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDto> bookingsByBooker = bookingService.getAllByOwner(userId, state);
        log.info("Получен список бронирований для вещей пользователя: {}", bookingsByBooker);
        return bookingsByBooker;
    }
}
