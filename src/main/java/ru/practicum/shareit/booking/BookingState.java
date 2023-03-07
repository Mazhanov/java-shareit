package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingState {
    All,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState getStatusFromString(String state) {
        switch (state) {
            case "ALL":
                return All;
            case "CURRENT":
                return CURRENT;
            case "PAST":
                return PAST;
            case "FUTURE":
                return FUTURE;
            case "WAITING":
                return WAITING;
            case "REJECTED":
                return REJECTED;
        }
        throw new UnsupportedStatusException();
    }
}
