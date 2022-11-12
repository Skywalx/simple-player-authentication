package com.skywalx.simpleplayerauthentication.event;

import com.skywalx.simpleplayerauthentication.event.BlacklistedEventExclusion;
import com.skywalx.simpleplayerauthentication.event.BlacklistedEventExecutor;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import com.skywalx.simpleplayerauthentication.storage.InMemoryAuthenticatedUserRepository;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.player.PlayerMoveEvent;
import org.junit.jupiter.api.Test;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BlacklistedEventExecutorTest {

    private static final UUID PLAYER_UUID = UUID.fromString("67240b03-a3b9-4006-84d2-b335a4917e4c");
    private final InMemoryAuthenticatedUserRepository playerAuthService = mock(InMemoryAuthenticatedUserRepository.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);

    @Test
    void execute_givenBlacklistedEvent_shouldCancel_whenPlayerIsNotLoggedIn() throws EventException {
        BlacklistedEventExecutor eventExecutor = new BlacklistedEventExecutor(playerAuthService, accountRepository);
        Player playerMock = mock(Player.class);
        when(playerMock.getUniqueId()).thenReturn(PLAYER_UUID);
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.of(new Account(PLAYER_UUID, "123")));
        when(playerAuthService.isAuthenticated(any(Account.class))).thenReturn(false);
        PlayerMoveEvent event = new PlayerMoveEvent(playerMock, null, null);

        eventExecutor.execute(null, event);

        assertThat(event.isCancelled()).isTrue();
    }

    @Test
    void execute_givenBlacklistedEvent_shouldCancel_whenAccountIsNotFound() {
        BlacklistedEventExecutor eventExecutor = new BlacklistedEventExecutor(playerAuthService, accountRepository);
        Player playerMock = mock(Player.class);
        when(playerMock.getUniqueId()).thenReturn(PLAYER_UUID);
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.empty());
        PlayerMoveEvent event = new PlayerMoveEvent(playerMock, null, null);

        eventExecutor.execute(null, event);

        assertThat(event.isCancelled()).isTrue();
    }

    @Test
    void execute_givenBlacklistedEvent_shouldNotCancel_whenNotAPlayerEvent() {
        BlacklistedEventExecutor eventExecutor = new BlacklistedEventExecutor(playerAuthService, accountRepository);
        EntityMountEvent event = new EntityMountEvent(null, null);

        eventExecutor.execute(null, event);

        assertThat(event.isCancelled()).isFalse();
    }

    @Test
    void execute_givenBlacklistedEvent_shouldNotCancel_whenExclusionIsMatched() {
        BlacklistedEventExecutor eventExecutor = new BlacklistedEventExecutor(playerAuthService, accountRepository, List.of(event -> true));
        Player playerMock = mock(Player.class);
        when(playerMock.getUniqueId()).thenReturn(PLAYER_UUID);
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.of(new Account(PLAYER_UUID, "123")));
        when(playerAuthService.isAuthenticated(any(Account.class))).thenReturn(false);
        PlayerMoveEvent event = new PlayerMoveEvent(playerMock, null, null);

        eventExecutor.execute(null, event);

        assertThat(event.isCancelled()).isFalse();
    }

    @Test
    void execute_givenBlacklistedEvent_shouldNotCancel_whenPlayerHasLoggedIn() {
        BlacklistedEventExecutor eventExecutor = new BlacklistedEventExecutor(playerAuthService, accountRepository);
        Location fromLocation = new Location(mock(World.class), 0, 0, 0);
        Location toLocation = new Location(mock(World.class), 1, 1, 1);
        Player playerMock = mock(Player.class);
        when(playerMock.getUniqueId()).thenReturn(PLAYER_UUID);
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.of(new Account(PLAYER_UUID, "123")));
        when(playerAuthService.isAuthenticated(any(Account.class))).thenReturn(true);
        PlayerMoveEvent event = new PlayerMoveEvent(playerMock, fromLocation, toLocation);

        eventExecutor.execute(null, event);

        assertThat(event.isCancelled()).isFalse();
    }

}