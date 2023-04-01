package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestService {
    ItemRequestDto create(ItemRequestDto dto, long requestorId, LocalDateTime created);

    List<ItemRequestDto> getAllOwn(long requestorId);

    List<ItemRequestDto> getAllRequestOtherUsers(long requestorId, Pageable pageable);

    ItemRequestDto getById(long requestId, long requestorId);
}
