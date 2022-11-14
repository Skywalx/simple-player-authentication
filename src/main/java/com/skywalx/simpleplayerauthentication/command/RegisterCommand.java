package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
import com.skywalx.simpleplayerauthentication.gui.RegistrationGui;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

@CommandAlias("register")
@Description("Command to register a new account.")
public class RegisterCommand extends BaseCommand {


    private final SimplePlayerAuthenticationPlugin plugin;
    private final AccountRepository accountRepository;
    private final HashingService hashingService;
    private final MessageConfiguration messageConfiguration;

    public RegisterCommand(SimplePlayerAuthenticationPlugin plugin, AccountRepository accountRepository, HashingService hashingService, MessageConfiguration messageConfiguration) {
        this.plugin = plugin;
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
        this.messageConfiguration = messageConfiguration;
    }

    @Default
    @Syntax("Usage: /register")
    public void onRegisterCommand(Player player) {
        Account account = new Account(player.getUniqueId(), "", hashingService);
        if (accountRepository.exists(account)) {
            messageConfiguration.send(MessageKey.ALREADY_REGISTERED, player);
            return;
        }

        RegistrationGui registrationGui = new RegistrationGui(plugin, accountRepository, hashingService, messageConfiguration);
        registrationGui.open(player);
    }
}
