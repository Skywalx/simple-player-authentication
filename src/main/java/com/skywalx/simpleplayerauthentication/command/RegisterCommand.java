package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.skywalx.simpleplayerauthentication.service.*;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

import java.io.IOException;

@CommandAlias("register")
public class RegisterCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final HashingService hashingService;

    public RegisterCommand(AccountRepository accountRepository, HashingService hashingService) {
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
    }

    @Default
    public void onRegisterCommand(Player player, String password, String confirmationPassword) {
        if (password.equals(confirmationPassword)) {
            Account account = new Account(player.getUniqueId(), password, hashingService);
            try {
                if (!accountRepository.exists(account)) {
                    accountRepository.save(account);
                    player.sendMessage("§6You have successfully registered " + player.getDisplayName());
                } else {
                    player.sendMessage("§6The account for " + player.getDisplayName() + " is already registered!");
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } else {
            player.sendMessage("§6The password are not the same! Please try again!");
        }
    }

}
