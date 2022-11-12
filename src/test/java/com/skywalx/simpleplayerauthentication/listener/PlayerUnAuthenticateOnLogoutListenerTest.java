package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PlayerUnAuthenticateOnLogoutListenerTest {

    private final AuthenticatedUserRepository authenticatedRepository = mock(AuthenticatedUserRepository.class);

    @Test
    void onPlayerLeave_whenPlayerIsLoggedIn_shouldUnAuthenticateOnQuit() {
        PlayerUnAuthenticateOnLogoutListener logoutListener = new PlayerUnAuthenticateOnLogoutListener(authenticatedRepository);

        logoutListener.onPlayerLeave(new PlayerQuitEvent(mock(Player.class), "whatever"));

        verify(authenticatedRepository).remove(any(Account.class));
    }

}