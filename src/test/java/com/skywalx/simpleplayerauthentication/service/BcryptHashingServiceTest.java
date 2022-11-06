package com.skywalx.simpleplayerauthentication.service;

import com.password4j.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BcryptHashingServiceTest {

    @Test
    void hash_shouldHashPasswordWithBcrypt() {
        String password = "minecraft123";
        BcryptHashingService bcryptHashingService = new BcryptHashingService();

        String hash = bcryptHashingService.hash(password);

        assertTrue(Password.check(password, hash).withBcrypt());
    }

    @Test
    void verify_shouldVerifyAPasswordHashedWithBcrypt() {
        String password = "minecraft123";
        String hash = Password.hash(password).withBcrypt().getResult();
        BcryptHashingService bcryptHashingService = new BcryptHashingService();

        boolean verification = bcryptHashingService.verify(password, hash);

        assertTrue(verification);
    }
}