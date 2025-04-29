package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto create(UserDto dto) {
        User user = UserMapper.toModel(dto);
        return UserMapper.toDto(repository.save(user));
    }

    @Override
    public UserDto update(UserDto dto) {
        User user = UserMapper.toModel(dto);
        if (!repository.findById(user.getId()).isPresent()) {
            throw new NoSuchElementException("Пользователь не найден: " + user.getId());
        }
        return UserMapper.toDto(repository.update(user));
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