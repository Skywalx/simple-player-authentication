package com.skywalx.simpleplayerauthentication.service;

import com.password4j.Password;

public class BcryptHashingService implements HashingService {
    @Override
    public String hash(String password) {
        return Password.hash(password)
                .withBcrypt()
                .getResult();
    }

    @Override
    public boolean verify(String password, String hash) {
        return Password.check(password, hash).withBcrypt();
    }
}
