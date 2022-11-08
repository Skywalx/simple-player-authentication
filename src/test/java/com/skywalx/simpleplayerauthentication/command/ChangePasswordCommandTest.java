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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChangePasswordCommandTest {

    private static final String PATH = "src/test/resources/accounts.yaml";
    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", hashingService);
    private Player player;
    private File file;
    private YamlConfiguration yamlConfiguration;

    @BeforeEach
    void setUp() throws IOException {
        file = new File(PATH);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(account.uuid());
        yamlConfiguration.set(player.getUniqueId() + ".password", "$argon2id$v=19$m=15360,t=2,p=1$IwXRsCNW77+9Sk/vv73vv70P77+9eO+/ve+/vRMCPO+/ve+/ve+/vVTvv73vv73vv71odnRQ77+977+9xrspHO+/ve+/ve+/ve+/vXQs77+9Tkkx77+977+9djjvv71ZV0hzO3Dvv71uVu+/ve+/vR0$m+wk/JZAONsXL21gMHhyH5UfGUoMBQU99nkRlmhnBl4");
        yamlConfiguration.save(file);
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(player.getUniqueId().toString(), null);
        yamlConfiguration.save(file);
    }

    @Test
    void onChangePasswordCommand_whencorrectOldPasswordIsGiven_shouldChangePassword() {
        String currentPassword = "minecraft123";
        String newPassword = "321tfarcenim";
        YamlAccountRepository accountRepository = new YamlAccountRepository(file, yamlConfiguration);
        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand(accountRepository, hashingService);

        changePasswordCommand.onChangePasswordCommand(player, currentPassword, newPassword, newPassword);

        Account updatedAccount = accountRepository.findByUuid(player.getUniqueId()).get();
        assertTrue(updatedAccount.doesPasswordMatch(newPassword));
    }
}