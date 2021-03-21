package com.test.microservices.multiplication.service;

import com.test.microservices.multiplication.domain.Multiplication;
import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;
import com.test.microservices.multiplication.domain.User;
import com.test.microservices.multiplication.repository.MultiplicationResultAttemptRepository;
import com.test.microservices.multiplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceImplTest {

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, userRepository, multiplicationResultAttemptRepository);
    }

    @Test
    public void createMultiplicationTest() {
        //given
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        //when
        Multiplication multiplication = multiplicationServiceImpl.createRandomMultiplication();

        //then
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50, 60);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt multiplicationResultAttemptVerified = new MultiplicationResultAttempt(user, multiplication, 3000, true);
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean result = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        //then
        assertThat(result).isTrue();
        verify(multiplicationResultAttemptRepository).save(multiplicationResultAttemptVerified);
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50, 60);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        BDDMockito.given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean result = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        //then
        assertThat(result).isFalse();
        BDDMockito.verify(multiplicationResultAttemptRepository).save(multiplicationResultAttempt);
    }
}
