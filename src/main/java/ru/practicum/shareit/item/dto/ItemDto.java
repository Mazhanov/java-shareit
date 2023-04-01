package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(groups = {Create.class}, message = "name cannot be missing")
    private String name;

    @NotBlank(groups = {Create.class}, message = "description cannot be missing")
    private String description;

    @NotNull(groups = {Create.class}, message = "available cannot be missing")
    private Boolean available;
    private User owner;
    private Long requestId;
    private List<CommentDto> comments;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;

    public ItemDto(Long id, String name, String description, Boolean available, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.requestId = null;
        this.comments = null;
        this.lastBooking = null;
        this.nextBooking = null;
    }
}
