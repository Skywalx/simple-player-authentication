package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class PlayerJoinListenerTest {

    private static final UUID PLAYER_UUID = UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d");
    private final MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);

    @Test
    void onPlayerJoin_whenPlayerIsNotAuthenticated_shouldInformUserToLogin() {
        AccountRepository accountRepository = mock(AccountRepository.class);
        AuthenticatedUserRepository authenticationRepository = mock(AuthenticatedUserRepository.class);
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.of(new Account(PLAYER_UUID, "minecraft123", new ArgonHashingService())));
        when(authenticationRepository.isAuthenticated(any())).thenReturn(false);
        Player player = mock(Player.class);
        when(player.getDisplayName()).thenReturn("username");
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);
        PlayerJoinListener playerJoinListener = new PlayerJoinListener(authenticationRepository, accountRepository, messageConfiguration);

        playerJoinListener.onPlayerJoin(new PlayerJoinEvent(player, "whatever"));

        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please login before proceeding\nUsername: &cusername\n&7Usage: &c/login&7"));
    }

    @Test
    void onPlayerJoin_whenPlayerIsAuthenticated_shouldNotInformUserToLogin() {
        AccountRepository accountRepository = mock(AccountRepository.class);
        AuthenticatedUserRepository authenticationRepository = mock(AuthenticatedUserRepository.class);
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.of(new Account(PLAYER_UUID, "minecraft123", new ArgonHashingService())));
        when(authenticationRepository.isAuthenticated(any())).thenReturn(true);
        Player player = mock(Player.class);
        when(player.getDisplayName()).thenReturn("username");
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);
        PlayerJoinListener playerJoinListener = new PlayerJoinListener(authenticationRepository, accountRepository, messageConfiguration);

        playerJoinListener.onPlayerJoin(new PlayerJoinEvent(player, "whatever"));

        verify(player, never()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please login before proceeding\nUsername: &cusername\n&7Usage: &c/login [password]&7"));
    }

    @Test
    void onPlayerJoin_whenPlayerIsNotRegistered_shouldInformUserToRegister() {
        AccountRepository accountRepository = mock(AccountRepository.class);
        AuthenticatedUserRepository authenticationRepository = mock(AuthenticatedUserRepository.class);
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.empty());
        Player player = mock(Player.class);
        when(player.getDisplayName()).thenReturn("username");
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);
        PlayerJoinListener playerJoinListener = new PlayerJoinListener(authenticationRepository, accountRepository, messageConfiguration);

        playerJoinListener.onPlayerJoin(new PlayerJoinEvent(player, "whatever"));

        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please register before proceeding\nUsername: &cusername\n&7Usage: &c/register&7"));
    }

}