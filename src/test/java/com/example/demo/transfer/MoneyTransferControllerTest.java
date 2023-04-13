package com.example.demo.transfer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MoneyTransferController.class)
class MoneyTransferControllerTest {
  @Autowired
  private MockMvc mockMvc;

  private final String baseUrl = "/api/money/transfer";

  @Test
  void transferMoney_correctFormat_returnsTransferredMoneyAmount() throws Exception {
    // Given
    String moneyAmount = "10.99";
    String expectedResponseBody = "10.99";

    // When
    mockMvc.perform(post(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(moneyAmount))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
  }

  @Test
  void transferMoney_moreThanTwoFractionalDigits_returnsRoundedTransferredMoneyAmount() throws Exception {
    // Given
    String moneyAmount = "10.34666611289012";
    String expectedResponseBody = "10.34";

    // When
    mockMvc.perform(post(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(moneyAmount))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
  }

  @Test
  void transferMoney_noFractionalDigits_returnsTransferredMoneyAmount() throws Exception {
    // Given
    String moneyAmount = "10";
    String expectedResponseBody = "10.00";

    // When
    mockMvc.perform(post(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(moneyAmount))
            .andExpect(status().isOk())
            .andExpect(content().string(expectedResponseBody));
  }

  @Test
  void transferMoney_negativeValue_returnsBadRequestResponse() throws Exception {
    // Given
    String invalidMoneyAmount = "-10";
    String expectedResponseBody = "Money to be transferred must be a positive number";

    // When
    mockMvc.perform(post(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidMoneyAmount))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(expectedResponseBody));
  }

  @Test
  void transferMoney_valueZero_returnsBadRequestResponse() throws Exception {
    // Given
    String invalidMoneyAmount = "0";
    String expectedResponseBody = "Money to be transferred must be a positive number";

    // When
    mockMvc.perform(post(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidMoneyAmount))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(expectedResponseBody));
  }

  @Test
  void transferMoney_invalidFormat_returnsBadRequestResponse() throws Exception {
    // Given
    String invalidMoneyAmount = "non-numerical";
    String expectedResponseBody = "Money to be transferred should be in numerical format";

    // When
    mockMvc.perform(post(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidMoneyAmount))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(expectedResponseBody));
  }
}
