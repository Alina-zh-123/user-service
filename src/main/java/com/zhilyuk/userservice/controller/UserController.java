package com.zhilyuk.userservice.controller;

import com.zhilyuk.userservice.dto.UserDto;
import com.zhilyuk.userservice.service.UserService;
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
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping("/usersFilteredPage")
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam String name, @RequestParam String surname, Pageable pageable) {
        return new ResponseEntity<>(userService.getAllUsersWithFilter(name, surname, pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestParam String name, @RequestParam String surname, @RequestParam String email) {
        return new ResponseEntity<>(userService.updateUser(id, name, surname, email), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> activateUser(@PathVariable Long id, @RequestParam Boolean activate) {
        userService.activateUser(id, activate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
