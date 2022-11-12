package com.skywalx.simpleplayerauthentication;

import co.aikar.commands.BukkitCommandManager;
import com.skywalx.simpleplayerauthentication.command.RegisterCommand;
import com.skywalx.simpleplayerauthentication.command.UnregisterCommand;
import com.skywalx.simpleplayerauthentication.config.DefaultConfiguration;
import com.skywalx.simpleplayerauthentication.listener.BlacklistedEventExecutor;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import com.skywalx.simpleplayerauthentication.storage.InMemoryAuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.storage.YamlAccountRepository;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class SimplePlayerAuthenticationPlugin extends JavaPlugin {

    private final Logger logger = this.getLogger();
    private final AuthenticatedUserRepository authenticatedUserRepository = new InMemoryAuthenticatedUserRepository();

    @Override
    public void onEnable() {
        HashingService hashingService = null;
        AccountRepository accountRepository = null;

        logger.info("Enabling plugin...");

        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        String hashingServiceName = this.getConfig().getString("hashing-algorithm");

        if (!"argon2".equalsIgnoreCase(hashingServiceName)) {
            logger.severe("The 'hashing-algorithm' has not been defined or is defined incorrectly!");
            this.getServer().shutdown();
        }

        hashingService = new ArgonHashingService();
        logger.info("- Hashing service: " + hashingServiceName);

        String accountRepositoryType = this.getConfig().getString("repository-type");

        if (!"yaml".equalsIgnoreCase(accountRepositoryType)) {
            logger.severe("The 'repository-type' has not been defined or is defined incorrectly!");
            this.getServer().shutdown();
        }

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

        BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(this);
        bukkitCommandManager.registerCommand(new RegisterCommand(accountRepository, hashingService));
        bukkitCommandManager.registerCommand(new UnregisterCommand(accountRepository, hashingService));

        DefaultConfiguration defaultConfiguration = new DefaultConfiguration(getConfig(), logger);
        List<Class<? extends PlayerEvent>> blacklistedPlayerEvents = defaultConfiguration.getBlacklistedEventsBeforeAuthentication();
        BlacklistedEventExecutor blacklistedEventExecutor = new BlacklistedEventExecutor(authenticatedUserRepository, accountRepository);

        blacklistedPlayerEvents.forEach(playerEventClass -> {
                    try {
                        Bukkit.getPluginManager().registerEvent(playerEventClass, new Listener() {
                            static HandlerList getHandlerList() {
                                return new HandlerList();
                            }
                        }, EventPriority.LOWEST, blacklistedEventExecutor, this);
                    } catch (IllegalPluginAccessException unknownHandlerListException) {
                        logger.severe("Could not creation event cancellation for " + playerEventClass.getSimpleName());
                    }
                });

        logger.info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        logger.info("Plugin has been disabled!");
    }
}
