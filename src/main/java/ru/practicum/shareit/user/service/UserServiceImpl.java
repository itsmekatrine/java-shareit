package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto create(UserDto dto) {
        boolean exists = repository.findAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()));
        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже используется: " + dto.getEmail()
            );
        }
        User user = UserMapper.toModel(dto);
        return UserMapper.toDto(repository.save(user));
    }

    @Override
    public UserDto update(UserDto dto) {
        User existing = repository.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден: " + dto.getId()));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            existing.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            String newEmail = dto.getEmail();
            boolean exists = repository.findAll().stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(newEmail) && !u.getId().equals(dto.getId()));
            if (exists) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email уже используется: " + newEmail);
            }
            existing.setEmail(dto.getEmail());
        }
        return UserMapper.toDto(repository.update(existing));
    }

    @Override
    public Optional<UserDto> getById(Long id) {
        return repository.findById(id)
                .map(UserMapper::toDto);
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}