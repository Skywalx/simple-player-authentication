package com.skywalx.simpleplayerauthentication.service;

import com.skywalx.simpleplayerauthentication.service.model.Account;

public interface AuthenticatedUserRepository {

    void add(Account account);

    boolean isAuthenticated(Account account);

    void remove(Account account);
}
