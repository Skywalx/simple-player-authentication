package com.skywalx.simpleplayerauthentication.command;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import com.skywalx.simpleplayerauthentication.storage.YamlAccountRepository;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;

class LoginCommandTest {

    private static final String PATH = "src/test/resources/accounts.yml";
    private static final UUID PLAYER_UUID = UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d");
    public static final String PLAINTEXT_PASSWORD = "minecraft123";

    private final AuthenticatedUserRepository authenticatedUserRepository = mock(AuthenticatedUserRepository.class);
    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(PLAYER_UUID, PLAINTEXT_PASSWORD, hashingService);
    private final SimplePlayerAuthenticationPlugin plugin = mock(SimplePlayerAuthenticationPlugin.class);

    private YamlAccountRepository accountRepository;
    private Player player;
    private File file;
    private YamlConfiguration yamlConfiguration;

    @BeforeEach
    void setup() {
        file = new File(PATH);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        accountRepository = new YamlAccountRepository(file, yamlConfiguration);
        player = mock(Player.class);
        when(player.getDisplayName()).thenReturn("username");
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(player.getUniqueId().toString(), null);
        yamlConfiguration.save(file);
    }

    @Test
    void onLoginCommand_whenPlayerIsNotRegistered_shouldInformPlayerToMakeAccount() {
        MessageConfiguration messageConfig = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);
        LoginCommand loginCommand = new LoginCommand(plugin, accountRepository, authenticatedUserRepository, messageConfig);

        loginCommand.onLoginCommand(player);

        verify(authenticatedUserRepository, never()).add(any(Account.class));
        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please register before proceeding\nUsername: &cusername\n&7Usage: &c/register&7"));
    }

    @Test
    void onLoginCommand_whenPlayerIsAlreadyLoggedIn_shouldInformPlayerThatHeIsAlreadyLoggedIn() {
        accountRepository.save(account);
        MessageConfiguration messageConfig = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);
        LoginCommand loginCommand = new LoginCommand(plugin, accountRepository, authenticatedUserRepository, messageConfig);
        when(authenticatedUserRepository.isAuthenticated(account)).thenReturn(true);

        loginCommand.onLoginCommand(player);

        verify(authenticatedUserRepository, never()).add(any(Account.class));
        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou already logged in!"));
    }


}