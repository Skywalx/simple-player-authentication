package com.skywalx.simpleplayerauthentication.service;

import com.password4j.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArgonHashingServiceTest {

    @Test
    void hash_shouldHashPasswordWithArgon2() {
        String password = "minecraft123";
        ArgonHashingService argonHashingService = new ArgonHashingService();

        String hash = argonHashingService.hash(password);

        assertTrue(Password.check(password, hash).withArgon2());
    }

    @Test
    void verify_shouldVerifyAPasswordHashedWithArgon2() {
        String password = "minecraft123";
        String hash = Password.hash(password).withArgon2().getResult();
        ArgonHashingService argonHashingService = new ArgonHashingService();

        boolean verification = argonHashingService.verify(password, hash);

        assertTrue(verification);
    }
}