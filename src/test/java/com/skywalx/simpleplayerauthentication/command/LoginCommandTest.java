package com.skywalx.simpleplayerauthentication.command;

import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoginCommandTest {

    private static final String PATH = "src/test/resources/accounts.yaml";
    private static final UUID PLAYER_UUID = UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d");
    public static final String PLAINTEXT_PASSWORD = "minecraft123";

    private final AuthenticatedUserRepository authenticatedUserRepository = mock(AuthenticatedUserRepository.class);
    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(PLAYER_UUID, PLAINTEXT_PASSWORD, hashingService);

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
        when(player.getUniqueId()).thenReturn(PLAYER_UUID);
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(player.getUniqueId().toString(), null);
        yamlConfiguration.save(file);
    }

    @Test
    void onLoginCommand_whenCredentialsAreCorrect_shouldAuthenticateUser() {
        accountRepository.save(account);
        LoginCommand loginCommand = new LoginCommand(accountRepository, authenticatedUserRepository);

        loginCommand.onLoginCommand(player, PLAINTEXT_PASSWORD);

        verify(authenticatedUserRepository).add(any(Account.class));
        verify(player).sendMessage("§7You succesfully logged in!");
    }

    @Test
    void onLoginCommand_whenPlayerIsNotRegistered_shouldInformPlayerToMakeAccount() {
        LoginCommand loginCommand = new LoginCommand(accountRepository, authenticatedUserRepository);

        loginCommand.onLoginCommand(player, PLAINTEXT_PASSWORD);

        verify(authenticatedUserRepository, never()).add(any(Account.class));
        verify(player).sendMessage("§7Please register before proceeding\\nUsername: §c%PLAYERNAME%\\n§7Usage: §c/register [password] [password]§7");
    }

    @Test
    void onLoginCommand_whenCredentialsAreInCorrect_shouldInformPlayerAndNotAuthenticate() {
        accountRepository.save(account);
        LoginCommand loginCommand = new LoginCommand(accountRepository, authenticatedUserRepository);

        loginCommand.onLoginCommand(player, "wrongpassword");

        verify(authenticatedUserRepository, never()).add(any(Account.class));
        verify(player).sendMessage("§cThe given password is incorrect!");
    }
}