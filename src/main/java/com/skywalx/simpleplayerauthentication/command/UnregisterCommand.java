package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
import com.skywalx.simpleplayerauthentication.gui.UnregisterGui;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("unregister")
@Description("Command to unregister an existing account.")
public class UnregisterCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final SimplePlayerAuthenticationPlugin plugin;
    private final AuthenticatedUserRepository authenticationRepository;
    private final MessageConfiguration messageConfiguration;

    public UnregisterCommand(SimplePlayerAuthenticationPlugin plugin, AccountRepository accountRepository, AuthenticatedUserRepository authenticationRepository, MessageConfiguration messageConfiguration) {
        this.accountRepository = accountRepository;
        this.plugin = plugin;
        this.authenticationRepository = authenticationRepository;
        this.messageConfiguration = messageConfiguration;
    }

    @Default
    @Syntax("Usage /unregister")
    public void onUnregisterCommand(Player player) {
        Optional<Account> optionalUserAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalUserAccount.isEmpty()) {
            messageConfiguration.send(MessageKey.REGISTER, player);
            return;
        }

        Account account = optionalUserAccount.get();
        UnregisterGui unregisterGui = new UnregisterGui(plugin, accountRepository, authenticationRepository, account, messageConfiguration);
        unregisterGui.open(player);
    }
}