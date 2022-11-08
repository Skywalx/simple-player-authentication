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

@CommandAlias("login")
@Description("Command to login into an account.")
public class LoginCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final HashingService hashingService;
    private final AuthenticatedUserRepository authenticatedUserRepository;

    public LoginCommand(AccountRepository accountRepository, HashingService hashingService, AuthenticatedUserRepository authenticatedUserRepository) {
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
        this.authenticatedUserRepository = authenticatedUserRepository;
    }

    @Default
    @Syntax("Usage: /login [password]")
    public void onLoginCommand(Player player, String password) {

    }
}
