package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import org.bukkit.entity.Player;

@CommandAlias("logout")
@Description("Command to logout from an account.")
public class LogoutCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final HashingService hashingService;
    private final AuthenticatedUserRepository authenticatedUserRepository;


    public LogoutCommand(AccountRepository accountRepository, HashingService hashingService, AuthenticatedUserRepository authenticatedUserRepository) {
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
        this.authenticatedUserRepository = authenticatedUserRepository;
    }

    @Default
    @Syntax("Usage: /logout")
    public void onLogoutCommand(Player player) {

    }
}
