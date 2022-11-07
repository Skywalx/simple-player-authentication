package com.skywalx.simpleplayerauthentication.service.model;

import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", hashingService);

    @Test
    void accountWithPlainText_shouldBeHashed() {
        String password = "minecraft123";

        String hashPassword = account.password();

        assertEquals(hashingService.hash(password), hashPassword);
    }

}