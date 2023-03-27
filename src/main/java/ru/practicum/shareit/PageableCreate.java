package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableCreate {
    public static Pageable pageableCreate(int from, int size) {
        return PageRequest.of((from / size), size);
    }
}
