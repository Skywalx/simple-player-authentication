package com.skywalx.simpleplayerauthentication.authentication;

import com.skywalx.simpleplayerauthentication.service.AuthenticationStrategy;

public class TotpAuthenticationStrategy implements AuthenticationStrategy {

    @Override
    public String create(String password) {
        
        return null;
    }

    @Override
    public boolean verify(String password, String hash) {
        return false;
    }

}
