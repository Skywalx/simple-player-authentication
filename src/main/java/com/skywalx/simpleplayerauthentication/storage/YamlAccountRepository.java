package com.skywalx.simpleplayerauthentication.storage;

import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


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
    public boolean login(Account account) {
        String uuid = account.uuid().toString();
        String password = account.password();
        if (this.yamlConfiguration.get(uuid) != null) {
            return password.equals(this.yamlConfiguration.get(uuid + ".password"));
        }
        return false;
    }
}
