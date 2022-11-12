package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
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

    public RegisterCommand(SimplePlayerAuthenticationPlugin plugin, AccountRepository accountRepository, HashingService hashingService) {
        this.plugin = plugin;
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
    }

    @Default
    @Syntax("Usage: /register")
    public void onRegisterCommand(Player player) {
        Account account = new Account(player.getUniqueId(), "", hashingService);
        if (accountRepository.exists(account)) {
            player.sendMessage("§6The account for " + player.getDisplayName() + " is already registered!");
            return;
        }

        RegistrationGui registrationGui = new RegistrationGui(plugin, accountRepository, hashingService);
        registrationGui.open(player);
    }
}
