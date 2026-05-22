package com.zhilyuk.userservice.mapper;

import com.zhilyuk.userservice.dto.UserDto;
import com.zhilyuk.userservice.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public User dtoToUser(UserDto userDto);
    public UserDto userToUserDto(User user);
}
