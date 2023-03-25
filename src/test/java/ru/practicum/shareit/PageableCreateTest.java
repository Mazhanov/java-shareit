package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;

class PageableCreateTest {

    @Test
    void pageableCreateTest_all() {
        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> PageableCreate.pageableCreate(-1, 0));
    }

}