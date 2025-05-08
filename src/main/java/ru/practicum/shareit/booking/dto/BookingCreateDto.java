package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {

    @NotNull(message = "Идентификатор вещи обязателен")
    private Long itemId;

    @NotNull(message = "Дата начала бронирования обязательна")
    private Instant start;

    @NotNull(message = "Дата окончания бронирования обязательна")
    private Instant end;
}
