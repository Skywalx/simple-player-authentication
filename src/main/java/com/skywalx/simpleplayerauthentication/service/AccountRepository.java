package com.skywalx.simpleplayerauthentication.service;

import com.skywalx.simpleplayerauthentication.service.model.Account;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {

    void save(Account account) throws IOException;

    Optional<Account> findByUuid(UUID uuid);

    void delete(Account account);

    boolean exists(Account account);

}
