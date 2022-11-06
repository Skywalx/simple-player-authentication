package com.skywalx.simpleplayerauthentication.service.model;


import com.skywalx.simpleplayerauthentication.service.HashingService;

import java.util.UUID;

public record Account(UUID uuid, String password, HashingService hashingService) {

    public Account {
        password = hashingService.hash(password);
    }
}
