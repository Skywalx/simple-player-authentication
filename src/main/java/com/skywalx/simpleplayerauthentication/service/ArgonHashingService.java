package com.skywalx.simpleplayerauthentication.service;

import com.password4j.Password;

public class ArgonHashingService implements HashingService {
    @Override
    public String hash(String password) {
        return Password.hash(password)
                .addRandomSalt()
                .withArgon2()
                .getResult();
    }

    @Override
    public boolean verify(String password, String hash) {
        return Password.check(password, hash).withArgon2();
    }
}
