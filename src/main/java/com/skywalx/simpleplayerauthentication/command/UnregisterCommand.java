package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

@CommandAlias("unregister")
public class UnregisterCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final HashingService hashingService;

    public UnregisterCommand(AccountRepository accountRepository, HashingService hashingService) {
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
    }

    @Default
    public void onUnregisterCommand(Player player, String password) {
        Account account = new Account(player.getUniqueId(), password, hashingService);
        if (accountRepository.exists(account)) {
            if (accountRepository.isCorrectPassword(account)) {
                accountRepository.delete(account);
                player.sendMessage("§6The account has been successfully unregistered!");
            } else {
                player.sendMessage("§6The given password is incorrect!");
            }
        } else {
            player.sendMessage("§6There is no account registered for " + player.getDisplayName() + "!");
        }
    }
}