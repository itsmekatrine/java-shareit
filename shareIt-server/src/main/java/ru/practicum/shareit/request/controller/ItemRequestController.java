package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemRequestCreateDto dto) {
        return requestService.create(userId, dto);
    }

    @GetMapping
    public List<ItemRequestDto> getOwnRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.findByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        return requestService.findById(userId, requestId);
    }
}
