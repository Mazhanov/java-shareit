package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.UnsupportedStatusException;

import javax.validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class PageableCreateTest {

    @Test
    void pageableCreateTest_all() {
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> PageableCreate.pageableCreate(-1, 0));
    }

}