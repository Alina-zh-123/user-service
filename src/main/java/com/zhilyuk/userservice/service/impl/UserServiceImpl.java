package com.zhilyuk.userservice.service.impl;

import com.zhilyuk.userservice.dto.UserDto;
import com.zhilyuk.userservice.exception.UserException;
import com.zhilyuk.userservice.mapper.UserMapper;
import com.zhilyuk.userservice.model.User;
import com.zhilyuk.userservice.repository.UserRepository;
import com.zhilyuk.userservice.service.UserService;
import com.zhilyuk.userservice.specification.UserSpecification;
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
    @CachePut(value = "userCache", key = "#result.id")
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        User res = userRepository.save(user);
        return userMapper.userToUserDto(res);
    }

    @Override
    @Cacheable(value = "userCache", key = "#id")
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
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
    public UserDto updateUser(Long id, String name, String surname, String email) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User is not found!"));
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "userCache", key = "#id")
    public void activateUser(Long id, Boolean activate) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User is not found!"));
        user.setActive(activate);
        userRepository.save(user);
    }
}
