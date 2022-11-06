package com.skywalx.simpleplayerauthentication.service;

import com.password4j.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScryptHashingServiceTest {

    @Test
    void hash_shouldHashPasswordWithScrypt() {
        String password = "minecraft123";
        ScryptHashingService scryptHashingService = new ScryptHashingService();

        String hash = scryptHashingService.hash(password);

        assertTrue(Password.check(password, hash).withScrypt());
    }

    @Test
    void verify_shouldVerifyAPasswordHashedWithScrypt() {
        String password = "minecraft123";
        String hash = Password.hash(password).withScrypt().getResult();
        ScryptHashingService scryptHashingService = new ScryptHashingService();

        boolean verification = scryptHashingService.verify(password, hash);

        assertTrue(verification);
    }
}