package com.skywalx.simpleplayerauthentication.command;

import com.skywalx.simpleplayerauthentication.service.AccountRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class RegisterCommandTest {

    private static final String PATH = "src/test/resources/accounts.yaml";
    private final HashingService hashingService = new ArgonHashingService();
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
        when(player.getUniqueId()).thenReturn(UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"));
        when(player.getDisplayName()).thenReturn("jensoman7");
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(player.getUniqueId().toString(), null);
        yamlConfiguration.save(file);
    }

    @Test
    void onRegisterPassword_whenPasswordsMatch_shouldRegisterAccount() {
        String password = "minecraft123";
        Account account = new Account(player.getUniqueId(), password, hashingService);
        RegisterCommand registerCommand = new RegisterCommand(accountRepository, hashingService);

        registerCommand.onRegisterCommand(player, password, password);

        assertTrue(accountRepository.exists(account));
        verify(player).sendMessage("§6You have successfully registered jensoman7!");
    }

    @Test
    void onRegisterPassword_whenPasswordsDoNotMatch_shouldReturnMessageToPlayer() {
        String password = "minecraft123";
        String incorrectConfirmationPassword = "arma3123";
        RegisterCommand registerCommand = new RegisterCommand(accountRepository, hashingService);

        registerCommand.onRegisterCommand(player, password, incorrectConfirmationPassword);

        verify(player).sendMessage("§6The password are not the same! Please try again!");
    }

    @Test
    void onRegisterPassword_whenAccountAlreadyExists_shouldReturnMessageToPlayer() throws IOException {
        String password = "minecraft123";
        RegisterCommand registerCommand = new RegisterCommand(accountRepository, hashingService);
        yamlConfiguration.set(player.getUniqueId() + ".password", hashingService.hash(password));
        yamlConfiguration.save(file);

        registerCommand.onRegisterCommand(player, password, password);

        verify(player).sendMessage("§6The account for jensoman7 is already registered!");
    }
}