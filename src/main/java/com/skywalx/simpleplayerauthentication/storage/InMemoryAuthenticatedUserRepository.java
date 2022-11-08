package com.skywalx.simpleplayerauthentication.storage;

import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;

import java.util.HashSet;
import java.util.Set;

public class InMemoryAuthenticatedUserRepository implements AuthenticatedUserRepository {

    private final Set<Account> authenticatedUsersSet = new HashSet<>();

    @Override
    public void add(Account account) {
        authenticatedUsersSet.add(account);
    }

    @Override
    public boolean isAuthenticated(Account account) {
        return authenticatedUsersSet.contains(account);
    }

    @Override
    public void remove(Account account) {
        authenticatedUsersSet.remove(account);
    }
}
