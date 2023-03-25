package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService{
    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto dto, long requestorId, LocalDateTime created) {
        User requestor = findUserByIdAndCheck(requestorId);
        ItemRequest itemRequest = RequestMapper.toItemRequest(dto);
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(created);
        return RequestMapper.toItemRequestDto(requestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getAllOwn(long requestorId) {
        findUserByIdAndCheck(requestorId);
        List<ItemRequestDto> itemRequests = requestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId)
                .stream()
                .map(RequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        if (!itemRequests.isEmpty()) {
            for (ItemRequestDto request : itemRequests) {
                addItemsForRequest(request);
            }
        }

        return itemRequests;
    }

    @Override
    public List<ItemRequestDto> getAllRequestOtherUsers(long requestorId, Pageable pageable) {
        findUserByIdAndCheck(requestorId);
        List<ItemRequestDto> itemRequests = requestRepository.findAllByRequestorIdNotOrderByCreatedDesc(requestorId, pageable)
                .stream()
                .map(RequestMapper::toItemRequestDto)
                .collect(Collectors.toList());

        if (!itemRequests.isEmpty()) {
            for (ItemRequestDto request : itemRequests) {
                addItemsForRequest(request);
            }
        }
        return itemRequests;
    }

    @Override
    public ItemRequestDto getById(long requestId, long requestorId) {
        findUserByIdAndCheck(requestorId);
        ItemRequestDto itemRequestDto = RequestMapper.toItemRequestDto(findRequestByIdAndCheck(requestId));
        addItemsForRequest(itemRequestDto);
        return itemRequestDto;
    }

    private User findUserByIdAndCheck(long id) { // Возвращает юзера по ID и проверяет наличие в БД
        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Пользователь с id" + id + " не найден"));
    }

    private ItemRequest findRequestByIdAndCheck(long id) { // Возвращает request по ID и проверяет наличие в БД
        return requestRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("Запрос с id" + id + " не найден"));
    }

    private void addItemsForRequest(ItemRequestDto itemRequestDto) {
        List<ItemDto> items = itemRepository.findAllByRequestIdOrderByIdAsc(itemRequestDto.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        itemRequestDto.setItems(items);
    }
}
