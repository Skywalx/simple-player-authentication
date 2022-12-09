package com.skywalx.simpleplayerauthentication.command;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.authentication.ArgonAuthenticationStrategy;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticationStrategy;
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

class UnregisterCommandTest {

    private static final String PATH = "src/test/resources/accounts.yml";
    private final AuthenticationStrategy authenticationStrategy = new ArgonAuthenticationStrategy();
    private final MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);
    private final Account account = new Account(java.util.UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", authenticationStrategy);
    private final AuthenticatedUserRepository authenticatedUserRepository = mock(AuthenticatedUserRepository.class);
    private final SimplePlayerAuthenticationPlugin plugin = mock(SimplePlayerAuthenticationPlugin.class);
    private Player player;
    private File file;
    private YamlConfiguration yamlConfiguration;
    private AccountRepository accountRepository;

    @BeforeEach
    void setup() {
        file = new File(PATH);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        accountRepository = new YamlAccountRepository(file, yamlConfiguration);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(account.uuid());
        try {
            yamlConfiguration.set(account.uuid().toString() + ".password", authenticationStrategy.create(account.password()));
            yamlConfiguration.save(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(player.getUniqueId().toString(), null);
        yamlConfiguration.save(file);
    }

    @Test
    void onUnregisterCommand_whenGivenAccountDoesNotExist_shouldReturnMessageToPlayer() {
        Player otherPlayer = mock(Player.class);
        when(otherPlayer.getUniqueId()).thenReturn(UUID.fromString("67240b03-a3b9-4006-84d2-b335a4917e4c"));
        when(otherPlayer.getDisplayName()).thenReturn("HungryDev");
        UnregisterCommand unregisterCommand = new UnregisterCommand(plugin, accountRepository, mock(AuthenticatedUserRepository.class), messageConfiguration);

        unregisterCommand.onUnregisterCommand(otherPlayer);

        verify(otherPlayer).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please register before proceeding\nUsername: &cHungryDev\n&7Usage: &c/register&7"));
    }
}