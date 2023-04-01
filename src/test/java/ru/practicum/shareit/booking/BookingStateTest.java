package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingStateTest {

    @Test
    void getStatusFromStringTest_all() {
        Assertions.assertEquals(BookingState.getStatusFromString("ALL"), BookingState.All);
    }

    @Test
    void getStatusFromStringTest_current() {
        Assertions.assertEquals(BookingState.getStatusFromString("CURRENT"), BookingState.CURRENT);
    }

    @Test
    void getStatusFromStringTest_past() {
        Assertions.assertEquals(BookingState.getStatusFromString("PAST"), BookingState.PAST);
    }

    @Test
    void getStatusFromStringTest_future() {
        Assertions.assertEquals(BookingState.getStatusFromString("FUTURE"), BookingState.FUTURE);
    }

    @Test
    void getStatusFromStringTest_waiting() {
        Assertions.assertEquals(BookingState.getStatusFromString("WAITING"), BookingState.WAITING);
    }

    @Test
    void getStatusFromStringTest_rejected() {
        Assertions.assertEquals(BookingState.getStatusFromString("REJECTED"), BookingState.REJECTED);
    }

    @Test
    void getStatusFromStringTest_Exception() {
        final UnsupportedStatusException exception = Assertions.assertThrows(
                UnsupportedStatusException.class,
                () -> BookingState.getStatusFromString("ALLALL"));
    }
}