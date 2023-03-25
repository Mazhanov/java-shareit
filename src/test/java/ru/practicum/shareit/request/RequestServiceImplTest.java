package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.PageableCreate;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private RequestServiceImpl requestService;

    @Test
    void createTest() {
        LocalDateTime date = LocalDateTime.now();
        User user = makeUser(1L);
        ItemRequestDto itemRequestDto = makeItemRequestDto(1L);
        itemRequestDto.setId(null);

        ItemRequest itemRequest = makeItemRequest(1L);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(date);

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.save(any()))
                .thenReturn(itemRequest);

        Assertions.assertEquals(requestService.create(itemRequestDto, 1L, date),
                RequestMapper.toItemRequestDto(itemRequest));
    }

    @Test
    void getAllOwnTest() {
        User user = makeUser(1L);

        List<ItemRequest> requests = List.of(makeItemRequest(1L));
        List<ItemRequestDto> requestOwn = List.of(RequestMapper.toItemRequestDto(makeItemRequest(1L)));
        requestOwn.get(0).setItems(List.of(ItemMapper.toItemDto(makeItem(1L))));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequestorIdOrderByCreatedDesc(anyLong()))
                .thenReturn(requests);
        when(itemRepository.findAllByRequestIdOrderByIdAsc(anyLong()))
                .thenReturn(List.of(makeItem(1L)));

        Assertions.assertEquals(requestService.getAllOwn(1L), requestOwn);
    }

    @Test
    void getAllRequestOtherUsersTest() {
        User user = makeUser(1L);

        List<ItemRequest> requests = List.of(makeItemRequest(1L));
        List<ItemRequestDto> requestOwn = List.of(RequestMapper.toItemRequestDto(makeItemRequest(1L)));
        requestOwn.get(0).setItems(List.of(ItemMapper.toItemDto(makeItem(1L))));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequestorIdNotOrderByCreatedDesc(anyLong(), any()))
                .thenReturn(requests);
        when(itemRepository.findAllByRequestIdOrderByIdAsc(anyLong()))
                .thenReturn(List.of(makeItem(1L)));

        Assertions.assertEquals(requestService.getAllRequestOtherUsers(1L, PageableCreate.pageableCreate(1, 10)), requestOwn);
    }

    @Test
    void getByIdTest() {
        User user = makeUser(1L);

        ItemRequest itemRequest = makeItemRequest(1L);
        ItemRequestDto itemRequestDto = RequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(List.of(ItemMapper.toItemDto(makeItem(1L))));

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestIdOrderByIdAsc(anyLong()))
                .thenReturn(List.of(makeItem(1L)));

        Assertions.assertEquals(requestService.getById(1L, 1L), itemRequestDto);
    }

    private ItemRequest makeItemRequest(long id) {
        return new ItemRequest(id, "description", makeUser(1L), LocalDateTime.MIN);
    }

    private ItemRequestDto makeItemRequestDto(long id) {
        return new ItemRequestDto(id, "description", LocalDateTime.MIN, List.of());
    }

    private Item makeItem(long id) {
        return new Item(id, "name", "description", false, null, 1L);
    }

    private User makeUser(long id) {
        return new User(id, "name", "email@email.ru");
    }
}