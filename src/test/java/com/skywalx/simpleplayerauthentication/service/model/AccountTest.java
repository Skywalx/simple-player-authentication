package com.skywalx.simpleplayerauthentication.service.model;

import com.skywalx.simpleplayerauthentication.authentication.ArgonAuthenticationStrategy;
import com.skywalx.simpleplayerauthentication.service.AuthenticationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {
    private final AuthenticationStrategy authenticationStrategy = new ArgonAuthenticationStrategy();
    private final Account account = new Account(UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", authenticationStrategy);

    @Test
    void accountWithPlainText_shouldBeHashed() {
        String password = "minecraft123";

        String hashPassword = account.password();

        assertTrue(authenticationStrategy.verify(password, hashPassword));
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "arma3123       |   false",
                    "minecraft123   |   true"
            }, delimiter = '|'
    )
    void doesPasswordMatch_withSamePlainTextPassword_shouldReturnTrue(String password, boolean expectsMatch) {
        boolean isSameAccount = account.doesPasswordMatch(password);

        assertEquals(expectsMatch, isSameAccount);
    }
}