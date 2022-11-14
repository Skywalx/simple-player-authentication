package com.skywalx.simpleplayerauthentication.service;

import com.password4j.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgonHashingServiceTest {

    private static final String PASSWORD = "minecraft123";
    private final ArgonHashingService argonHashingService = new ArgonHashingService();

    @Test
    void hash_shouldHashPasswordWithArgon2() {
        String hash = argonHashingService.hash(PASSWORD);

        assertTrue(Password.check(PASSWORD, hash).withArgon2());
    }

    @Test
    void verify_shouldVerifyAPasswordHashedWithArgon2() {
        String hash = Password.hash(PASSWORD)
                .addRandomSalt()
                .withArgon2()
                .getResult();

        boolean verification = argonHashingService.verify(PASSWORD, hash);

        assertTrue(verification);
    }
}