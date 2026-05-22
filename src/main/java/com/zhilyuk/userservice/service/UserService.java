package com.zhilyuk.userservice.service;

import com.zhilyuk.userservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    public UserDto createUser(UserDto userDto);
    public UserDto getUserById(Long id);
    public Page<UserDto> getAllUsersWithFilter(String name, String surname, Pageable pageable);
    public UserDto updateUser(Long id, String name, String surname, String email);
    public void activateUser(Long id, Boolean activate);
}


