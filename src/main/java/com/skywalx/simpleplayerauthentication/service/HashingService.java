package com.skywalx.simpleplayerauthentication.service;


public interface HashingService {

    String hash(String password);

    boolean verify(String password, String hash);

}
