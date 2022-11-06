package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

@CommandAlias("register")
public class RegisterCommand extends BaseCommand {

    private final AccountRepository accountRepository;

    public RegisterCommand(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Default
    public void onRegister(Player player, String password, String confirmationPassword) {
        if (password.equals(confirmationPassword)) {
            // AccountRepository.save()
        } else {
            player.sendMessage("§6The password are not the same! Please try again!");
        }
    }

}
