package com.innowise.userservice.unit;

import com.innowise.userservice.dto.UserDto;
import com.innowise.userservice.exception.UserException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.entity.User;
import com.innowise.userservice.repository.UserRepository;
import com.innowise.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    private User user1;
    private User user2;
    private UserDto userDto1;
    private UserDto userDto2;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setName("Arina");
        user1.setSurname("Maximova");
        user1.setEmail("qwerty@gmail.com");
        user1.setBirthDate(LocalDate.parse("2007-01-14"));
        user1.setActive(true);
        user1.setPaymentCards(new ArrayList<>());

        user2 = new User();
        user2.setId(2L);
        user2.setName("Marina");
        user2.setSurname("Aximova");
        user2.setEmail("ytrewq@gmail.com");
        user2.setBirthDate(LocalDate.parse("2007-01-14"));
        user2.setActive(true);
        user2.setPaymentCards(new ArrayList<>());

        userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setName("Arina");
        userDto1.setSurname("Maximova");
        userDto1.setEmail("qwerty@gmail.com");
        userDto1.setBirthDate(LocalDate.parse("2007-01-14"));
        userDto1.setActive(false);
        userDto1.setPaymentCards(new ArrayList<>());

        userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setName("Marina");
        userDto2.setSurname("Aximova");
        userDto2.setEmail("ytrewq@gmail.com");
        userDto2.setBirthDate(LocalDate.parse("2007-01-14"));
        userDto2.setActive(false);
        userDto2.setPaymentCards(new ArrayList<>());
    }

    @Test
    void createUser_shouldCreateUser() {
        when(userMapper.dtoToUser(userDto1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userRepository.save(user1)).thenReturn(user1);

        UserDto userDto = userService.createUser(userDto1);
        assertEquals(userDto, userDto1);

        verify(userMapper).dtoToUser(userDto1);
        verify(userMapper).userToUserDto(user1);
        verify(userRepository).save(user1);
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);

        UserDto userDto = userService.getUserById(1L);
        assertEquals(userDto1, userDto);

        verify(userRepository).findById(1L);
        verify(userMapper).userToUserDto(user1);
    }

    @Test
    void getUserById_shouldThrowUserException() {
        when(userRepository.findById(1234L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserException.class, () -> {
            userService.getUserById(1234L);
        });
        assertEquals("User is not found!", exception.getMessage());

        verify(userRepository).findById(1234L);
    }

    @Test
    void getAllUsersWithFilter_ShouldReturnPageUserDto() {
        Pageable pageable = PageRequest.of(0, 10);

        List<User> users = List.of(user1);
        Page<User> pageUsers = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(
                ArgumentMatchers.<Specification<User>>any(),
                eq(pageable)
        )).thenReturn(pageUsers);

        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);

        Page<UserDto> pageUserDto = userService.getAllUsersWithFilter("Arina", "Maximova", pageable);

        assertEquals(1, pageUserDto.getTotalPages());
        assertEquals(1, pageUserDto.getTotalElements());

        verify(userRepository).findAll(
                ArgumentMatchers.<Specification<User>>any(),
                eq(pageable)
        );
    }

    @Test
    void updateUser_shouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        when(userMapper.userToUserDto(user1)).thenReturn(userDto2);

        UserDto newUserDto = new UserDto();
        newUserDto.setName("Marina");
        newUserDto.setSurname("Aximova");
        newUserDto.setEmail("ytrewq@gmail.com");

        UserDto userDto = userService.updateUser(1L, newUserDto);
        assertEquals(userDto2, userDto);

        verify(userRepository).findById(1L);
        verify(userRepository).save(user1);
        verify(userMapper).userToUserDto(user1);
    }

    @Test
    void activateUser() {
        doAnswer(invocation -> {
            user2.setActive(true);
            return null;
        }).when(userRepository).activateUserById(2L, true);

        userService.activateUser(2L, true);
        assertTrue(user2.isActive());

        verify(userRepository).activateUserById(2L, true);
    }

    @Test
    void deactivateUser() {
        doAnswer(invocation -> {
            user1.setActive(false);
            return null;
        }).when(userRepository).activateUserById(1L, false);

        userService.activateUser(1L, false);
        assertFalse(user1.isActive());

        verify(userRepository).activateUserById(1L, false);
    }
}