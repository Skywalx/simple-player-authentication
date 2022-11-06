package com.skywalx.simpleplayerauthentication.service;

import com.password4j.Password;

public class ScryptHashingService implements HashingService {


    @Override
    public String hash(String password) {
        return Password.hash(password)
                .withScrypt()
                .getResult();
    }

    @Override
    public boolean verify(String password, String hash) {
        return Password.check(password, hash).withScrypt();
    }
}
