package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(CreateBookingDto dto, long userId);

    BookingDto changeStatusBooking(long userId, long bookingId, boolean status);

    BookingDto getBooking(long userId, long bookingId);

    List<BookingDto> getAllByBooker(long userId, BookingState state);

    List<BookingDto> getAllByOwner(long userId, BookingState state);
}
