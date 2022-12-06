package com.skywalx.simpleplayerauthentication.command;

import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class LogoutCommandTest {

    public static final UUID PLAYER_UUID = UUID.fromString("67240b03-a3b9-4006-84d2-b335a4917e4c");
    public static final Account ACCOUNT = new Account(PLAYER_UUID, "minecraft123", new ArgonHashingService());
    private final AuthenticatedUserRepository authenticationRepository = mock(AuthenticatedUserRepository.class);
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);


    @Test
    void onLogoutCommand_givenPlayerHasAccountAndIsLoggedIn_shouldLogout() {
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.of(ACCOUNT));
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);
        LogoutCommand logoutCommand = new LogoutCommand(accountRepository, authenticationRepository, messageConfiguration);

        logoutCommand.onLogoutCommand(player);

        verify(authenticationRepository).remove(ACCOUNT);
    }

    @Test
    void onLogoutCommand_givenPlayerDoesNotHaveAccountAndLoggedIn_shouldKickUser() {
        when(accountRepository.findByUuid(PLAYER_UUID)).thenReturn(Optional.empty());
        Player player = mock(Player.class);
        when(player.getDisplayName()).thenReturn("username");
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);
        LogoutCommand logoutCommand = new LogoutCommand(accountRepository, authenticationRepository, messageConfiguration);

        logoutCommand.onLogoutCommand(player);

        verify(player).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&7Please register before proceeding\nUsername: &cusername\n&7Usage: &c/register&7"));
    }
}