package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotNull(message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Некорректный формат электронной почты")
    private String email;
}

