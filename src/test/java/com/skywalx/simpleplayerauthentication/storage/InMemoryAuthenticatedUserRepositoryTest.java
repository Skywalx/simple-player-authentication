package com.skywalx.simpleplayerauthentication.storage;

import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryAuthenticatedUserRepositoryTest {

    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(java.util.UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", hashingService);

    @Test
    void add() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();

        inMemoryAuthenticatedUserRepository.add(account);

        assertTrue(inMemoryAuthenticatedUserRepository.isAuthenticated(account));
    }

    @Test
    void isAuthenticated() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();
        inMemoryAuthenticatedUserRepository.add(account);

        boolean authenticated = inMemoryAuthenticatedUserRepository.isAuthenticated(account);

        assertTrue(authenticated);
    }

    @Test
    void remove() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();
        inMemoryAuthenticatedUserRepository.add(account);

        inMemoryAuthenticatedUserRepository.remove(account);

        assertFalse(inMemoryAuthenticatedUserRepository.isAuthenticated(account));
    }
}