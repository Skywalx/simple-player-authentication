package com.skywalx.simpleplayerauthentication.storage;

import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;


public class YamlAccountRepository implements AccountRepository {

    private final File file;
    private final YamlConfiguration yamlConfiguration;

    public YamlAccountRepository(File file, YamlConfiguration yamlConfiguration) {
        this.file = file;
        this.yamlConfiguration = yamlConfiguration;
    }

    @Override
    public void save(Account account) {
        String uuid = account.uuid().toString();
        try {
            this.yamlConfiguration.set(uuid + ".password", account.password());
            this.yamlConfiguration.save(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public Optional<Account> findByUuid(UUID uuid) {
        if (uuid == null) return Optional.empty();
        String password = this.yamlConfiguration.getString(uuid.toString() + ".password");
        if (password == null) return Optional.empty();
        return Optional.of(new Account(uuid, password));
    }

    @Override
    public void delete(Account account) {
        String uuid = account.uuid().toString();
        try {
            this.yamlConfiguration.set(uuid, null);
            this.yamlConfiguration.save(file);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public boolean exists(Account account) {
        return this.yamlConfiguration.get(account.uuid().toString()) != null;
    }

}
