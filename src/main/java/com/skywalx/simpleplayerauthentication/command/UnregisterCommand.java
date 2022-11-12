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

import java.util.Optional;

@CommandAlias("unregister")
@Description("Command to unregister an existing account.")
public class UnregisterCommand extends BaseCommand {

    private final AccountRepository accountRepository;

    public UnregisterCommand(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Default
    @Syntax("Usage /unregister [password]")
    public void onUnregisterCommand(Player player, String password) {
        Optional<Account> optionalUserAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalUserAccount.isEmpty()) {
            player.sendMessage("§6There is no account registered for " + player.getDisplayName() + "!");
            return;
        }

        Account account = optionalUserAccount.get();
        if (!account.doesPasswordMatch(password)) {
            player.sendMessage("§6The given password is incorrect!");
            return;
        }

        accountRepository.delete(account);
        player.sendMessage("§6The account has been successfully unregistered!");
    }
}