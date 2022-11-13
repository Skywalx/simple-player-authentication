package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
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
    private final MessageConfiguration messageConfiguration;

    public LogoutCommand(AccountRepository accountRepository, AuthenticatedUserRepository authenticationRepository, MessageConfiguration messageConfiguration) {
        this.accountRepository = accountRepository;
        this.authenticationRepository = authenticationRepository;
        this.messageConfiguration = messageConfiguration;
    }

    @Default
    @Syntax("Usage: /logout")
    public void onLogoutCommand(Player player) {
        Optional<Account> optionalAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalAccount.isEmpty()) {
            player.kickPlayer(messageConfiguration.getFormattedMessage(MessageKey.REGISTER, player.getDisplayName()));
            authenticationRepository.remove(new Account(player.getUniqueId(), ""));
            return;
        }

        Account account = optionalAccount.get();
        messageConfiguration.send(MessageKey.SUCCESSFUL_LOGOUT, player);
        authenticationRepository.remove(account);
    }
}
