package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.service.*;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

import java.io.IOException;

@CommandAlias("register")
@Description("Command to register a new account.")
public class RegisterCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final HashingService hashingService;

    public RegisterCommand(AccountRepository accountRepository, HashingService hashingService) {
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
    }

    @Default
    @Syntax("Usage: /register [password] [password]")
    public void onRegisterCommand(Player player, String password, String confirmationPassword) {
        Account account = new Account(player.getUniqueId(), password, hashingService);

        if (accountRepository.exists(account)) {
            player.sendMessage("§6The account for " + player.getDisplayName() + " is already registered!");
            return;
        }

        if (!password.equals(confirmationPassword)) {
            player.sendMessage("§6The password are not the same! Please try again!");
            return;
        }

        try {
            accountRepository.save(account);
            player.sendMessage("§6You have successfully registered " + player.getDisplayName() + "!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
