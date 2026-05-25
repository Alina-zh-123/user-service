package com.innowise.userservice.mapper;

import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User dtoToUser(UserDto userDto);
    public UserDto userToUserDto(User user);
}
