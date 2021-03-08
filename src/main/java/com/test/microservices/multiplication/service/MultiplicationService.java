package com.test.microservices.multiplication.service;

import com.test.microservices.multiplication.domain.Multiplication;
import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationService {

    Multiplication createRandomMultiplication();

    boolean checkAttempt(final MultiplicationResultAttempt multiplicationResultAttempt);
}
