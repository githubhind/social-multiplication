package com.test.microservices.multiplication.service;

import com.test.microservices.multiplication.domain.Multiplication;
import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;

    @Autowired
    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @Override
    public Multiplication createRandomMultiplication() {
        int factorA = randomGeneratorService.generateRandomFactor();
        int factorB = randomGeneratorService.generateRandomFactor();
        return new Multiplication(factorA, factorB);
    }

    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt) {
        return multiplicationResultAttempt.getResultAttempt() ==
                multiplicationResultAttempt.getMultiplication().getFactorA() *
                        multiplicationResultAttempt.getMultiplication().getFactorB();
    }

}
