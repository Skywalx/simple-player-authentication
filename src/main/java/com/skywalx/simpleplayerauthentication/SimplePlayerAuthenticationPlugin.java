package com.skywalx.simpleplayerauthentication;

import co.aikar.commands.BukkitCommandManager;
import com.skywalx.simpleplayerauthentication.command.RegisterCommand;
import com.skywalx.simpleplayerauthentication.command.UnregisterCommand;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.storage.YamlAccountRepository;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SimplePlayerAuthenticationPlugin extends JavaPlugin {

    private final Logger logger = this.getLogger();

    @Override
    public void onEnable() {
        HashingService hashingService = null;
        AccountRepository accountRepository = null;

        logger.info("Enabling plugin...");

        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        String hashingServiceName = this.getConfig().getString("hashing-algorithm");
        if (hashingServiceName.equalsIgnoreCase("argon2")) {
            hashingService = new ArgonHashingService();
            logger.info("- Hashing service: " + hashingServiceName);
        } else {
            logger.severe("The 'hashing-algorithm' has not been defined or is defined incorrectly!");
        }

        String accountRepositoryType = this.getConfig().getString("repository-type");
        if (accountRepositoryType.equalsIgnoreCase("yaml")) {
            File accountsFile = new File(this.getDataFolder(), "accounts.yaml");
            if (!accountsFile.exists()) {
                try {
                    accountsFile.createNewFile();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(accountsFile);
            accountRepository = new YamlAccountRepository(accountsFile, yamlConfiguration);
            logger.info("- Repository type: " + accountRepositoryType);
        } else {
            logger.severe("The 'repository-type' has not been defined or is defined incorrectly!");
        }
        BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(this);
        bukkitCommandManager.registerCommand(new RegisterCommand(accountRepository, hashingService));
        bukkitCommandManager.registerCommand(new UnregisterCommand(accountRepository, hashingService));
        logger.info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("Plugin has been disabled!");
    }
}
