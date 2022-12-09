package com.skywalx.simpleplayerauthentication.service;


public interface AuthenticationStrategy {

    String create(String password);

    boolean verify(String password, String hash);

}
