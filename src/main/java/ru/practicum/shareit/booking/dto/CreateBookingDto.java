package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingDto {
    private Long id;

    @NotNull(message = "start cannot be missing")
    private LocalDateTime start;

    @NotNull(message = "end cannot be missing")
    private LocalDateTime end;

    @NotNull(message = "itemId cannot be missing")
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
}
