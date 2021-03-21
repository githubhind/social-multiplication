package com.test.microservices.multiplication.service;

import com.test.microservices.multiplication.domain.Multiplication;
import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;
import com.test.microservices.multiplication.domain.User;
import com.test.microservices.multiplication.repository.MultiplicationResultAttemptRepository;
import com.test.microservices.multiplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;
    private UserRepository userRepository;
    private MultiplicationResultAttemptRepository multiplicationResultAttemptRepository;

    @Autowired
    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService, UserRepository userRepository, MultiplicationResultAttemptRepository multiplicationResultAttemptRepository) {
        this.randomGeneratorService = randomGeneratorService;
        this.userRepository = userRepository;
        this.multiplicationResultAttemptRepository = multiplicationResultAttemptRepository;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt) {
        boolean verification = multiplicationResultAttempt.getResultAttempt() ==
                multiplicationResultAttempt.getMultiplication().getFactorA() *
                        multiplicationResultAttempt.getMultiplication().getFactorB();
        Optional<User> user = userRepository.findByAlias(multiplicationResultAttempt.getUser().getAlias());
        MultiplicationResultAttempt multiplicationResultAttemptVerified = new MultiplicationResultAttempt(
                user.orElse(multiplicationResultAttempt.getUser()),
                multiplicationResultAttempt.getMultiplication(),
                multiplicationResultAttempt.getResultAttempt(),
                verification
        );
        multiplicationResultAttemptRepository.save(multiplicationResultAttemptVerified);
        return verification;
    }

}
