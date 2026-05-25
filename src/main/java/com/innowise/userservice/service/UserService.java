package com.innowise.userservice.service;

import com.innowise.userservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    public UserDto createUser(UserDto userDto);
    public UserDto getUserById(Long id);
    public Page<UserDto> getAllUsersWithFilter(String name, String surname, Pageable pageable);
    public UserDto updateUser(Long id, UserDto userDto);
    public void activateUser(Long id, Boolean activate);
}


