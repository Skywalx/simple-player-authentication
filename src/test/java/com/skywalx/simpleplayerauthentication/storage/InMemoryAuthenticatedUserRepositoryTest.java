package com.skywalx.simpleplayerauthentication.storage;

import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryAuthenticatedUserRepositoryTest {

    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(java.util.UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", hashingService);
    private final Account differentAccountWithSameCredentials = new Account(java.util.UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123");

    @Test
    void add_whenUserIsAddedToInMemoryAuthenticatedRepository_repositoryShouldContainTheUser() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();

        inMemoryAuthenticatedUserRepository.add(account);

        assertTrue(inMemoryAuthenticatedUserRepository.isAuthenticated(account));
    }

    @Test
    void add_whenUserIsAddedToInMemoryAuthenticatedRepository_repositoryShouldContainAnDifferentAccountWithSameCredentials() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();

        inMemoryAuthenticatedUserRepository.add(account);

        assertTrue(inMemoryAuthenticatedUserRepository.isAuthenticated(differentAccountWithSameCredentials));
    }

    @Test
    void isAuthenticated_whenUserThatExistsInInMemoryAuthenticatedUserRepositoryIsGiven_shouldReturnTrue() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();
        inMemoryAuthenticatedUserRepository.add(account);

        boolean authenticated = inMemoryAuthenticatedUserRepository.isAuthenticated(account);

        assertTrue(authenticated);
    }

    @Test
    void remove_whenUserIsRemovedFromInMemoryAuthenticatedRepository_repositoryShouldNotContainTheUserAnymore() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();
        inMemoryAuthenticatedUserRepository.add(account);

        inMemoryAuthenticatedUserRepository.remove(account);

        assertFalse(inMemoryAuthenticatedUserRepository.isAuthenticated(account));
    }

    @Test
    void remove_whenUserWithSameUuidIsRemovedFromInMemoryAuthenticatedRepository_repositoryShouldNotContainTheUserAnymore() {
        InMemoryAuthenticatedUserRepository inMemoryAuthenticatedUserRepository = new InMemoryAuthenticatedUserRepository();
        inMemoryAuthenticatedUserRepository.add(account);

        inMemoryAuthenticatedUserRepository.remove(new Account(account.uuid(), ""));

        assertFalse(inMemoryAuthenticatedUserRepository.isAuthenticated(account));
    }

}