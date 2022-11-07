package com.skywalx.simpleplayerauthentication.service;

import com.skywalx.simpleplayerauthentication.service.model.Account;

import java.io.IOException;

public interface AccountRepository {

    void save(Account account) throws IOException;

    void delete(Account account);

    boolean exists(Account account);

    boolean login(Account account);
}
