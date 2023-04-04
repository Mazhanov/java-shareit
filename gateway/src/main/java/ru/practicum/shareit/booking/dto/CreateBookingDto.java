package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingDto {
    private Long id;

    @FutureOrPresent(message = "start cannot be missing")
    @NotNull
    private LocalDateTime start;

    @Future(message = "end cannot be missing")
    @NotNull
    private LocalDateTime end;
    private Long itemId;
}
