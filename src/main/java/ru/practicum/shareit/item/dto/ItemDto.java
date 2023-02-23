package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;

    @NotBlank(groups = {Create.class}, message = "name cannot be missing")
    private String name;

    @NotBlank(groups = {Create.class}, message = "description cannot be missing")
    private String description;

    @NotNull(groups = {Create.class}, message = "available cannot be missing")
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
