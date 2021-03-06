package com.test.microservices.multiplication.service;

import com.test.microservices.multiplication.domain.Multiplication;
import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;
import com.test.microservices.multiplication.domain.User;
import com.test.microservices.multiplication.event.EventDispatcher;
import com.test.microservices.multiplication.event.MultiplicationSolvedEvent;
import com.test.microservices.multiplication.repository.MultiplicationResultAttemptRepository;
import com.test.microservices.multiplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
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

    @Mock
    private EventDispatcher eventDispatcher;

    private MultiplicationServiceImpl multiplicationServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        multiplicationServiceImpl = new MultiplicationServiceImpl(randomGeneratorService, userRepository, multiplicationResultAttemptRepository, eventDispatcher);
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
        verify(eventDispatcher).send(BDDMockito.any(MultiplicationSolvedEvent.class));
    }

    @Test
    public void checkWrongAttemptTest() {
        //given
        User user = new User("john_doe");
        Multiplication multiplication = new Multiplication(50, 60);
        MultiplicationResultAttempt multiplicationResultAttempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.empty());

        //when
        boolean result = multiplicationServiceImpl.checkAttempt(multiplicationResultAttempt);

        //then
        assertThat(result).isFalse();
        BDDMockito.verify(multiplicationResultAttemptRepository).save(multiplicationResultAttempt);
        verify(eventDispatcher).send(BDDMockito.any(MultiplicationSolvedEvent.class));
    }

    @Test
    public void retrieveStatsTest(){
        //given
        User user = new User("john");
        Multiplication multiplication = new Multiplication(10, 11);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 110, true);
        given(multiplicationResultAttemptRepository.findTop5ByUserAliasOrderByIdDesc("john")).willReturn(List.of(attempt));

        //when
        List<MultiplicationResultAttempt> lastAttemptsForUser = multiplicationServiceImpl.getStatsForUser("john");

        //then
        assertThat(lastAttemptsForUser).isNotEmpty();
        assertThat(lastAttemptsForUser).containsOnly(attempt);
        verify(multiplicationResultAttemptRepository).findTop5ByUserAliasOrderByIdDesc("john");
    }
}
