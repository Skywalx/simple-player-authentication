package com.skywalx.simpleplayerauthentication.service.model;

import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void accountWithPlainText_shouldBeHashed() {
        ArgonHashingService argonHashingService = new ArgonHashingService();
        UUID uuid = UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d");
        String password = "minecraft123";
        Account account = new Account(uuid, password, argonHashingService);

        String hashPassword = account.password();

        assertEquals(argonHashingService.hash(password), hashPassword);
    }

}