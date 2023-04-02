package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingStateTest {

    @Test
    void getStatusFromStringTest_all() {
        Assertions.assertEquals(BookingState.from("ALL"), BookingState.ALL);
    }

    @Test
    void getStatusFromStringTest_current() {
        Assertions.assertEquals(BookingState.from("CURRENT"), BookingState.CURRENT);
    }

    @Test
    void getStatusFromStringTest_past() {
        Assertions.assertEquals(BookingState.from("PAST"), BookingState.PAST);
    }

    @Test
    void getStatusFromStringTest_future() {
        Assertions.assertEquals(BookingState.from("FUTURE"), BookingState.FUTURE);
    }

    @Test
    void getStatusFromStringTest_waiting() {
        Assertions.assertEquals(BookingState.from("WAITING"), BookingState.WAITING);
    }

    @Test
    void getStatusFromStringTest_rejected() {
        Assertions.assertEquals(BookingState.from("REJECTED"), BookingState.REJECTED);
    }

    @Test
    void getStatusFromStringTest_Exception() {
        final UnsupportedStatusException exception = Assertions.assertThrows(
                UnsupportedStatusException.class,
                () -> BookingState.from("ALLALL"));
    }
}