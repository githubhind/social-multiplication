package com.test.microservices.multiplication.service;

import com.test.microservices.multiplication.domain.Multiplication;
import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;
import com.test.microservices.multiplication.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class MultiplicationServiceImplTest {

    @Mock
    private RandomGeneratorService randomGeneratorService;

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        //when
        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        //then
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
        //assertThat(multiplication.getResult()).isEqualTo(1500);
    }

    @Test
    public void checkAttemptWhenResultCorrectTest() {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50, 60);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3000);

        //when
        boolean result = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50, 60);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3010);

        //when
        boolean result = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        //then
        assertThat(result).isFalse();
    }
}
