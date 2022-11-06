package com.skywalx.simpleplayerauthentication;

import co.aikar.commands.BukkitCommandManager;
import com.skywalx.simpleplayerauthentication.command.RegisterCommand;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.storage.YamlAccountRepository;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SimplePlayerAuthenticationPlugin extends JavaPlugin {

    private final Logger logger = this.getLogger();

    private final FileConfiguration config = this.getConfig();
    private final BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(this);

    private HashingService hashingService = null;
    private AccountRepository accountRepository = null;

    @Override
    public void onEnable() {
        logger.info("Enabling plugin...");
        String hashingServiceName = config.getString("hashing-algorithm");
        if (hashingServiceName.equalsIgnoreCase("argon2")) {
            hashingService = new ArgonHashingService();
        } else {
            logger.warning("The 'hashing-algorithm' has not been defined or is defined incorrectly!");
        }

        String accountRepositoryType = config.getString("repository-type");
        if (accountRepositoryType.equalsIgnoreCase("yaml")) {
            File accountsFile = new File(this.getDataFolder(), "accounts.yaml");
            if (accountsFile.exists()) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(accountsFile);
                accountRepository = new YamlAccountRepository(accountsFile, yamlConfiguration);
            }
        } else {
            logger.warning("The 'repository-type' has not been defined or is defined incorrectly!");
        }

        bukkitCommandManager.registerCommand(new RegisterCommand(accountRepository, hashingService));
        logger.info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
