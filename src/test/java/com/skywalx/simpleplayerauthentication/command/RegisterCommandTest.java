package com.skywalx.simpleplayerauthentication.command;

import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import com.skywalx.simpleplayerauthentication.storage.YamlAccountRepository;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RegisterCommandTest {

    private static final String PATH = "src/test/resources/accounts.yaml";
    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(java.util.UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", hashingService);
    private Player player;
    private File file;
    private YamlConfiguration yamlConfiguration;

    @BeforeEach
    void setup() {
        file = new File(PATH);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(account.uuid());
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(player.getUniqueId().toString(), null);
        yamlConfiguration.save(file);
    }

    @Test
    void onRegisterPassword_shouldRegisterAccountWhenPasswordsMatch() {
        String password = "minecraft123";
        String confirmationPassword = "minecraft123";
        YamlAccountRepository accountRepository = new YamlAccountRepository(file, yamlConfiguration);
        RegisterCommand registerCommand = new RegisterCommand(accountRepository, hashingService);

        registerCommand.onRegisterCommand(player, password, confirmationPassword);

        assertTrue(accountRepository.exists(account));
    }
}