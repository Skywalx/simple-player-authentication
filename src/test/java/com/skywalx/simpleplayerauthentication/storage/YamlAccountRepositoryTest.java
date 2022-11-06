package com.skywalx.simpleplayerauthentication.storage;

import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class YamlAccountRepositoryTest {

    private static final String UUID = "de0ba13e-59ee-4b7f-903b-658b40d36e7d";
    private static final String PATH = "src/test/resources/accounts.yaml";
    private final ArgonHashingService argonHashingService = new ArgonHashingService();
    private File file;
    private YamlConfiguration yamlConfiguration;

    @BeforeEach
    void setup() {
        file = new File(PATH);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(UUID, null);
        yamlConfiguration.save(file);
    }

    @Test
    void save_shouldSaveAccountToAccountsYamlFile() {
        Account account = new Account(java.util.UUID.fromString(UUID), "minecraft123", argonHashingService);
        YamlAccountRepository yamlAccountRepository = new YamlAccountRepository(file, yamlConfiguration);

        yamlAccountRepository.save(account);

        assertNotNull(yamlConfiguration.get(UUID));
    }

    @Test
    void delete_shouldDeleteAccountFromAccountsYamlFile() {
        Account account = new Account(java.util.UUID.fromString(UUID), "minecraft123", argonHashingService);
        YamlAccountRepository yamlAccountRepository = new YamlAccountRepository(file, yamlConfiguration);
        yamlConfiguration.set(UUID + ".password", account.password());

        yamlAccountRepository.delete(account);

        assertNull(yamlConfiguration.get(UUID));
    }

    @Test
    void login_whenPasswordIsCorrect_shouldReturnTrue() {
        Account account = new Account(java.util.UUID.fromString(UUID), "minecraft123", argonHashingService);
        YamlAccountRepository yamlAccountRepository = new YamlAccountRepository(file, yamlConfiguration);
        yamlConfiguration.set(UUID + ".password", account.password());

        boolean login = yamlAccountRepository.login(account);

        assertTrue(login);
    }

}