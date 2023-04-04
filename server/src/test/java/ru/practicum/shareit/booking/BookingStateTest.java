package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookingStateTest {

    @Test
    void getStatusFromStringTest_all() {
        Assertions.assertEquals(BookingState.from("ALL").get(), BookingState.ALL);
    }

    @Test
    void getStatusFromStringTest_current() {
        Assertions.assertEquals(BookingState.from("CURRENT").get(), BookingState.CURRENT);
    }

    @Test
    void getStatusFromStringTest_past() {
        Assertions.assertEquals(BookingState.from("PAST").get(), BookingState.PAST);
    }

    @Test
    void getStatusFromStringTest_future() {
        Assertions.assertEquals(BookingState.from("FUTURE").get(), BookingState.FUTURE);
    }

    @Test
    void getStatusFromStringTest_waiting() {
        Assertions.assertEquals(BookingState.from("WAITING").get(), BookingState.WAITING);
    }

    @Test
    void getStatusFromStringTest_rejected() {
        Assertions.assertEquals(BookingState.from("REJECTED").get(), BookingState.REJECTED);
    }
}