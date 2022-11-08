package com.skywalx.simpleplayerauthentication.service.model;


import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;

import java.util.UUID;

public record Account(UUID uuid, String password, HashingService hashingService) {

    public Account {
        if (hashingService != null) {
            password = hashingService.hash(password);
        } else {
            hashingService = new ArgonHashingService();
        }
    }

    public Account(UUID uuid, String hashedPassword) {
        this(uuid, hashedPassword, null);
    }

    public boolean doesPasswordMatch(String plainTextPassword) {
        return hashingService.verify(plainTextPassword, password);
    }

}
