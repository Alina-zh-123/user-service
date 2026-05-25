package com.innowise.userservice.controller;

import com.innowise.userservice.dto.OnActivate;
import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            Pageable pageable) {
        return new ResponseEntity<>(userService.getAllUsersWithFilter(name, surname, pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.updateUser(id, userDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> activateUser(
            @PathVariable Long id,
            @Validated(OnActivate.class) @RequestBody UserDto userDto) {
        userService.activateUser(id, userDto.isActive());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
