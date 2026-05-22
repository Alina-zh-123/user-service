package com.zhilyuk.userservice.integration;

import com.zhilyuk.userservice.dto.UserDto;
import com.zhilyuk.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    private UserDto userDto;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
    }

    @BeforeEach
    public void setup(){
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Arina");
        userDto.setSurname("Maximova");
        userDto.setEmail("qwerty@gmail.com");
        userDto.setBirthDate(LocalDate.parse("2007-01-14"));
        userDto.setActive(false);
        userDto.setPaymentCard(new ArrayList<>());
    }

    @Test
    public void getUserById_shouldReturnUser() throws Exception {
        String responseBody = mockMvc.perform(post("/users/{id}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Arina")))
                .andExpect(jsonPath("$.surname", is("Maximova")))
                .andReturn().getResponse().getContentAsString();

        UserDto createdUserDto = objectMapper.readValue(responseBody, UserDto.class);
        assertTrue(userRepository.findById(createdUserDto.getId()).isPresent());
    }

    @Test
    void getUserById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", 1234L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User is not found!")));
    }
}
