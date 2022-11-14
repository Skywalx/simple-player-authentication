package com.skywalx.simpleplayerauthentication.storage;

import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class YamlAccountRepositoryTest {

    private static final String PATH = "src/test/resources/accounts.yaml";
    private final HashingService hashingService = new ArgonHashingService();
    private final Account account = new Account(UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"), "minecraft123", hashingService);
    private File file;
    private YamlConfiguration yamlConfiguration;

    @BeforeEach
    void setup() {
        file = new File(PATH);
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    @AfterEach
    void tearDown() throws IOException {
        yamlConfiguration.set(account.uuid().toString(), null);
        yamlConfiguration.save(file);
    }

    @Test
    void save_shouldSaveAccountToAccountsYamlFile() {
        YamlAccountRepository yamlAccountRepository = new YamlAccountRepository(file, yamlConfiguration);

        yamlAccountRepository.save(account);

        assertNotNull(yamlConfiguration.get(account.uuid().toString()));
    }

    @Test
    void findByUuid_whenAnAccountExists_shouldReturnAccount() throws IOException {
        AccountRepository accountRepository = new YamlAccountRepository(file, yamlConfiguration);

        accountRepository.save(account);
        Optional<Account> foundAccount = accountRepository.findByUuid(UUID.fromString("de0ba13e-59ee-4b7f-903b-658b40d36e7d"));

        assertTrue(foundAccount.isPresent());
    }

    @Test
    void delete_shouldDeleteAccountFromAccountsYamlFile() {
        YamlAccountRepository yamlAccountRepository = new YamlAccountRepository(file, yamlConfiguration);
        yamlConfiguration.set(account.uuid().toString() + ".password", account.password());

        yamlAccountRepository.delete(account);

        assertNull(yamlConfiguration.get(account.uuid().toString()));
    }

    @Test
    void exists_whenAccountExists_shouldReturnTrue() {
        YamlAccountRepository yamlAccountRepository = new YamlAccountRepository(file, yamlConfiguration);
        yamlConfiguration.set(account.uuid().toString() + ".password", account.password());

        boolean exists = yamlAccountRepository.exists(account);

        assertTrue(exists);
    }
}