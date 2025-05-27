package ru.practicum.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    @NotBlank(message = "Отзыв не может быть пустым")
    private String text;

    @NotNull(message = "Отзыв не может быть без автора")
    private String authorName;

    private LocalDateTime created;
}
