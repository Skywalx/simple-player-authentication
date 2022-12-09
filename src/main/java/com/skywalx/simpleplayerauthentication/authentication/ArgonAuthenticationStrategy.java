package com.skywalx.simpleplayerauthentication.authentication;

import com.password4j.Password;
import com.skywalx.simpleplayerauthentication.service.AuthenticationStrategy;

public class ArgonAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public String create(String password) {
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
