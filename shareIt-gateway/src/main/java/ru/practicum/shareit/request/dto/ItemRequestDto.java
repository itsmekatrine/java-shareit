package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotNull(message = "Запрос не может быть пустым")
    @Size(min = 1)
    private String description;

    @NotNull(message = "Дата создания обязательна")
    private LocalDateTime created;
    private List<ItemResponseDto> items;
}
