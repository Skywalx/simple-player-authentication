package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

@CommandAlias("unregister")
@Description("Command to unregister an existing account.")
public class UnregisterCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final HashingService hashingService;

    public UnregisterCommand(AccountRepository accountRepository, HashingService hashingService) {
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
    }

    @Default
    @Syntax("Usage /unregister [password]")
    public void onUnregisterCommand(Player player, String password) {
        Account account = new Account(player.getUniqueId(), password, hashingService);
        if (!accountRepository.exists(account)) {
            player.sendMessage("§6There is no account registered for " + player.getDisplayName() + "!");
            return;
        }

        if (!account.doesPasswordMatch(password)) {
            player.sendMessage("§6The given password is incorrect!");
            return;
        }

        accountRepository.delete(account);
        player.sendMessage("§6The account has been successfully unregistered!");
    }
}