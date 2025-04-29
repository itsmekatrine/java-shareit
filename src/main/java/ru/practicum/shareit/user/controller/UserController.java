package ru.practicum.shareit.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }


}
