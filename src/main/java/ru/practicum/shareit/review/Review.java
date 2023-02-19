package ru.practicum.shareit.review;

import lombok.Data;

@Data
public class Review {
    private Long id;
    private Long itemId;
    private Long userTenantId;
    private String textReview;
}
