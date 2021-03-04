package com.test.microservices.multiplication.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomGeneratorServiceImplTest {

    private RandomGeneratorServiceImpl randomGeneratorServiceImpl;

    @BeforeEach
    public void setUp(){
        randomGeneratorServiceImpl = new RandomGeneratorServiceImpl();
    }

    @Test
    public void shouldGenerateFactor(){

        List<Integer> result = IntStream.range(0, 1000).boxed().map(i -> randomGeneratorServiceImpl.generateRandomFactor()).collect(Collectors.toList());

        assertThat(result).containsAnyElementsOf(IntStream.range(11, 100).boxed().collect(Collectors.toList()));
    }
}
