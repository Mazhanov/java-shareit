package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

class PageableCreateTest {
    @Test
    void pageableCreateTest_all() {
        assertEquals(PageableCreate.pageableCreate(10, 5), PageRequest.of(2, 5));
    }
}