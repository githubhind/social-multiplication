package com.test.microservices.multiplication.controler;

import com.test.microservices.multiplication.domain.MultiplicationResultAttempt;
import com.test.microservices.multiplication.service.MultiplicationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
public final class MultiplicationResultAttemptController {

    private final MultiplicationService multiplicationService;

    @Autowired
    public MultiplicationResultAttemptController(MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @PostMapping
    public ResponseEntity<ResultResponse> checkMultiplicationResultAttempt(@RequestBody MultiplicationResultAttempt multiplicationResultAttempt){
        return ResponseEntity.ok(new ResultResponse(multiplicationService.checkAttempt(multiplicationResultAttempt)));
    }

    @RequiredArgsConstructor
    @Getter
    public static final class ResultResponse {
        private final boolean correct;
    }
}
