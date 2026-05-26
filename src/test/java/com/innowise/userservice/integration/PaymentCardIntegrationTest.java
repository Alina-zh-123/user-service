package com.innowise.userservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.userservice.dto.PaymentCardDto;
import com.innowise.userservice.entity.PaymentCard;
import com.innowise.userservice.repository.PaymentCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class PaymentCardIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentCardRepository paymentCardRepository;
    private PaymentCardDto paymentCardDto;

    @Autowired
    private ObjectMapper objectMapper;

    private Long createdCardId;

    @Container
    static PostgreSQLContainer<?> postgresSQLContainer = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgresSQLContainer::getPassword);
    }

    @BeforeEach
    void setup() throws Exception {
        paymentCardDto = new PaymentCardDto();
        paymentCardDto.setNumber("4691798612364563");
        paymentCardDto.setHolder("Arina");
        paymentCardDto.setExpirationDate(LocalDate.parse("2030-03-01"));
        paymentCardDto.setActive(true);

        MvcResult result = mockMvc.perform(post("/payment-cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentCardDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        PaymentCardDto createdCard = objectMapper.readValue(response, PaymentCardDto.class);
        createdCardId = createdCard.getId();
    }

    @Test
    public void getPaymentCardById_shouldReturnPaymentCard() throws Exception {
        mockMvc.perform(get("/payment-cards/{id}", createdCardId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("4691798612364563"))
                .andExpect(jsonPath("$.holder").value("Arina"))
                .andExpect(jsonPath("$.expirationDate").value("2030-03-01"))
                .andExpect(jsonPath("$.active").value(true));

        assertTrue(paymentCardRepository.findById(createdCardId).isPresent());
    }

    @Test
    void getPaymentCardById_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/payment-cards/{id}", 1234L))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Payment Card is not found!")));
    }

    @Test
    void updatePaymentCardById_shouldReturnUpdatedPaymentCard() throws Exception {
        String paymentCardDto = """
        {
          "number": "1234567812345678",
          "holder": "Updated Arina",
          "expirationDate": "2031-03-01",
          "active": true
        }
        """;

        mockMvc.perform(put("/payment-cards/{id}", createdCardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(paymentCardDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holder").value("Updated Arina"))
                .andExpect(jsonPath("$.expirationDate").value("2031-03-01"));
    }

    @Test
    void activatePaymentCard_shouldDeactivatePaymentCard() throws Exception {
        mockMvc.perform(patch("/payment-cards/{id}", createdCardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"activate\": false}"))
                .andExpect(status().isNoContent());

        PaymentCard updatedCard = paymentCardRepository.findById(createdCardId).orElseThrow();
        assertFalse(updatedCard.isActive());
    }
}
