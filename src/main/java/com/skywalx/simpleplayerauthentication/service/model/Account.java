package com.skywalx.simpleplayerauthentication.service.model;


import com.skywalx.simpleplayerauthentication.authentication.ArgonAuthenticationStrategy;
import com.skywalx.simpleplayerauthentication.service.AuthenticationStrategy;

import java.util.Objects;
import java.util.UUID;

public record Account(UUID uuid, String password, AuthenticationStrategy authenticationStrategy) {

    public Account {
        if (authenticationStrategy != null) {
            password = authenticationStrategy.create(password);
        } else {
            authenticationStrategy = new ArgonAuthenticationStrategy();
        }
    }

    public Account(UUID uuid, String hashedPassword) {
        this(uuid, hashedPassword, null);
    }

    public boolean doesPasswordMatch(String plainTextPassword) {
        return authenticationStrategy.verify(plainTextPassword, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(uuid, account.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
