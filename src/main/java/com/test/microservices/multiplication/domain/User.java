package com.test.microservices.multiplication.domain;

import lombok.*;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class User {

    private final String alias;

    public User() {
        this.alias = null;
    }
}
