package com.skywalx.simpleplayerauthentication.command;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.authentication.ArgonAuthenticationStrategy;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticationStrategy;
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

class RegisterCommandTest {

    private static final String PATH = "src/test/resources/accounts.yml";
    private final MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);
    private final AuthenticationStrategy authenticationStrategy = new ArgonAuthenticationStrategy();
    private Player player;
    private File file;
    private YamlConfiguration yamlConfiguration;
    private AccountRepository accountRepository;
    private SimplePlayerAuthenticationPlugin plugin = mock(SimplePlayerAuthenticationPlugin.class);


    @BeforeEach
    void setup() {
        file = new File(PATH);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        accountRepository = new YamlAccountRepository(file, yamlConfiguration);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"));
        when(player.getDisplayName()).thenReturn("jensoman7");
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(player.getUniqueId().toString(), null);
        yamlConfiguration.save(file);
    }

    // TODO: Find some way to test if inventory is opened, AnvilGUI seems to use packets so we might be able to check if a certain packet is sent

    @Test
    void onRegisterPassword_whenAccountAlreadyExists_shouldReturnMessageToPlayer() throws IOException {
        String password = "minecraft123";
        RegisterCommand registerCommand = new RegisterCommand(plugin, accountRepository, authenticationStrategy, messageConfiguration);
        yamlConfiguration.set(player.getUniqueId() + ".password", authenticationStrategy.create(password));
        yamlConfiguration.save(file);

        registerCommand.onRegisterCommand(player);

        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe account for jensoman7 is already registered!"));
    }
}