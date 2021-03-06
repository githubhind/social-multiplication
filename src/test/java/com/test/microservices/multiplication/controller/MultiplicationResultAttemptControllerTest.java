package com.test.microservices.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.microservices.multiplication.controler.MultiplicationResultAttemptController;
import com.test.microservices.multiplication.domain.Multiplication;
import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;
import com.test.microservices.multiplication.domain.User;
import com.test.microservices.multiplication.service.MultiplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {

    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonMultiplicationResultAttempt;
    private JacksonTester<List<MultiplicationResultAttempt>> jsonMultiplicationResultAttemptList;

    @BeforeEach
    public void setup(){
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testVerificationWhenCorrectAttempt() throws Exception {
        baseVerificationAttemptTest(true);
    }

    @Test
    public void testVerificationWhenIncorrectAttempt() throws Exception {
        baseVerificationAttemptTest(false);
    }

    private void baseVerificationAttemptTest(boolean correct) throws Exception {
        //given
        User user = new User("John");
        Multiplication multiplication = new Multiplication(10, 20);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 200, correct);
        given(multiplicationService.checkAttempt(any(MultiplicationResultAttempt.class))).willReturn(correct);

        //when
        MockHttpServletResponse response = mvc.perform(post("/results").contentType(MediaType.APPLICATION_JSON).content(jsonMultiplicationResultAttempt.write(multiplicationResultAttempt).getJson())).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonMultiplicationResultAttempt.write(multiplicationResultAttempt).getJson());
    }

    @Test
    public void retrieveStatsForUserTest() throws Exception {
        //given
        User user = new User("john");
        Multiplication multiplication = new Multiplication(10, 12);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 120, true);
        List<MultiplicationResultAttempt> multiplicationResultAttemptList = List.of(multiplicationResultAttempt);
        given(multiplicationService.getStatsForUser("john")).willReturn(multiplicationResultAttemptList);

        //when
        MockHttpServletResponse response = mvc.perform(get("/results").param("alias", "john")).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(jsonMultiplicationResultAttemptList.write(multiplicationResultAttemptList).getJson());
        verify(multiplicationService).getStatsForUser("john");
    }
}
