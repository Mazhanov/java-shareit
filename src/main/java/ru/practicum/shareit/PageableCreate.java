package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.validation.ValidationException;

public class PageableCreate {
    public static Pageable pageableCreate(int from, int size) {
        if (size == 0 || from < 0 || size < 1) {
            throw new ValidationException("incorrect parametrs for pages");
        }

        return PageRequest.of((from / size), size);
    }
}
