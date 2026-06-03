package com.innowise.userservice.service;

import com.innowise.userservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    Page<UserDto> getAllUsersWithFilter(String name, String surname, Pageable pageable);
    UserDto updateUser(Long id, UserDto userDto);
    void activateUser(Long id, Boolean activate);
}


