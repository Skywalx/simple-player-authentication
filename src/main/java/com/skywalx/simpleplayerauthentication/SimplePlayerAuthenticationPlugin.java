package com.skywalx.simpleplayerauthentication;

import co.aikar.commands.BukkitCommandManager;
import com.skywalx.simpleplayerauthentication.command.LoginCommand;
import com.skywalx.simpleplayerauthentication.command.LogoutCommand;
import com.skywalx.simpleplayerauthentication.command.RegisterCommand;
import com.skywalx.simpleplayerauthentication.command.UnregisterCommand;
import com.skywalx.simpleplayerauthentication.config.DefaultConfiguration;
import com.skywalx.simpleplayerauthentication.listener.PlayerJoinListener;
import com.skywalx.simpleplayerauthentication.listener.PlayerUnAuthenticateOnLogoutListener;
import com.skywalx.simpleplayerauthentication.listener.exclusions.BlacklistedEventExclusion;
import com.skywalx.simpleplayerauthentication.listener.exclusions.BlacklistedEventExecutor;
import com.skywalx.simpleplayerauthentication.listener.exclusions.BlacklistedLoginEventExclusion;
import com.skywalx.simpleplayerauthentication.listener.exclusions.BlacklistedRegisterEventExclusion;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.ArgonHashingService;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.storage.InMemoryAuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.storage.YamlAccountRepository;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class SimplePlayerAuthenticationPlugin extends JavaPlugin {

    private final Logger logger = this.getLogger();
    private final AuthenticatedUserRepository authenticatedUserRepository = new InMemoryAuthenticatedUserRepository();

    @Override
    public void onEnable() {
        logger.info("Enabling plugin...");

        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();

        HashingService hashingService = configuredHashingService();
        AccountRepository accountRepository = configuredAccountRepository();

        registerCommands(hashingService, accountRepository);
        registerListeners(accountRepository);

        logger.info("Plugin has been enabled!");
    }

    private AccountRepository configuredAccountRepository() {
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
        AccountRepository accountRepository = new YamlAccountRepository(accountsFile, yamlConfiguration);
        logger.info("- Repository type: " + accountRepositoryType);
        return accountRepository;
    }

    private HashingService configuredHashingService() {
        String hashingServiceName = this.getConfig().getString("hashing-algorithm");

        if (!"argon2".equalsIgnoreCase(hashingServiceName)) {
            logger.severe("The 'hashing-algorithm' has not been defined or is defined incorrectly!");
            this.getServer().shutdown();
        }

        HashingService hashingService = new ArgonHashingService();
        logger.info("- Hashing service: " + hashingServiceName);
        return hashingService;
    }

    private void registerCommands(HashingService hashingService, AccountRepository accountRepository) {
        BukkitCommandManager bukkitCommandManager = new BukkitCommandManager(this);
        bukkitCommandManager.registerCommand(new RegisterCommand(this, accountRepository, hashingService));
        bukkitCommandManager.registerCommand(new UnregisterCommand(this, accountRepository, authenticatedUserRepository));
        bukkitCommandManager.registerCommand(new LoginCommand(this, accountRepository, authenticatedUserRepository));
        bukkitCommandManager.registerCommand(new LogoutCommand(accountRepository, authenticatedUserRepository));
    }

    private void registerListeners(AccountRepository accountRepository) {
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration(getConfig(), logger);
        List<Class<? extends PlayerEvent>> blacklistedPlayerEvents = defaultConfiguration.getBlacklistedEventsBeforeAuthentication();
        List<BlacklistedEventExclusion> blacklistExclusions = List.of(new BlacklistedLoginEventExclusion(), new BlacklistedRegisterEventExclusion());
        BlacklistedEventExecutor blacklistedEventExecutor = new BlacklistedEventExecutor(authenticatedUserRepository, accountRepository, blacklistExclusions);

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

        if (getConfig().getBoolean("log-player-out-on-leave")) {
            Bukkit.getPluginManager().registerEvents(new PlayerUnAuthenticateOnLogoutListener(authenticatedUserRepository), this);
        }
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(authenticatedUserRepository, accountRepository), this);
    }

    @Override
    public void onDisable() {
        logger.info("Plugin has been disabled!");
    }
}
