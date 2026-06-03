package com.innowise.userservice.service.impl;

import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.exception.UserException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.UserService;
import com.innowise.userservice.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        User res = userRepository.save(user);
        return userMapper.userToUserDto(res);
    }

    @Override
    @Cacheable(value = "userCache", key = "#id")
    public UserDto getUserById(Long id) {
        User user = userRepository.findByIdWithCards(id)
                .orElseThrow(() -> new UserException("User is not found!"));
        return userMapper.userToUserDto(user);
    }

    @Override
    public Page<UserDto> getAllUsersWithFilter(String name, String surname, Pageable pageable) {
        Specification<User> spec = Specification
                .where(UserSpecification.hasName(name))
                .and(UserSpecification.hasSurname(surname));
        Page<User> res = userRepository.findAll(spec, pageable);
        return res.map(userMapper::userToUserDto);
    }

    @Override
    @Transactional
    @CachePut(value = "userCache", key = "#id")
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findByIdWithCards(id)
                .orElseThrow(() -> new UserException("User is not found!"));
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userCache", key = "#id")
    public void activateUser(Long id, Boolean activate) {
        userRepository.activateUserById(id, activate);
    }
}
