package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("login")
@Description("Command to login into an account.")
public class LoginCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final AuthenticatedUserRepository authenticatedUserRepository;

    public LoginCommand(AccountRepository accountRepository, AuthenticatedUserRepository authenticatedUserRepository) {
        this.accountRepository = accountRepository;
        this.authenticatedUserRepository = authenticatedUserRepository;
    }

    @Default
    @Syntax("Usage: /login [password]")
    public void onLoginCommand(Player player, String password) {
        Optional<Account> optionalAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalAccount.isEmpty()) {
            player.sendMessage("§7Please register before proceeding\\nUsername: §c%PLAYERNAME%\\n§7Usage: §c/register [password] [password]§7");
            return;
        }

        Account account = optionalAccount.get();
        if (!account.doesPasswordMatch(password)) {
            player.sendMessage("§cThe given password is incorrect!");
            return;
        }

        authenticatedUserRepository.add(account);
        player.sendMessage("§7You succesfully logged in!");
    }
}
