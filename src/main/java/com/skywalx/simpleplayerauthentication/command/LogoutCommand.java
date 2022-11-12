package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("logout")
@Description("Command to logout from an account.")
public class LogoutCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final AuthenticatedUserRepository authenticationRepository;

    public LogoutCommand(AccountRepository accountRepository, AuthenticatedUserRepository authenticationRepository) {
        this.accountRepository = accountRepository;
        this.authenticationRepository = authenticationRepository;
    }

    @Default
    @Syntax("Usage: /logout")
    public void onLogoutCommand(Player player) {
        Optional<Account> optionalAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalAccount.isEmpty()) {
            player.kickPlayer("§cYour account no longer exists!");
            authenticationRepository.remove(new Account(player.getUniqueId(), ""));
            return;
        }

        Account account = optionalAccount.get();
        player.sendMessage("§7You are now logged out!");
        authenticationRepository.remove(account);
    }
}
