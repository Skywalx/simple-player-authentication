package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.gui.LoginGui;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("login")
@Description("Command to login into an account.")
public class LoginCommand extends BaseCommand {

    private final AccountRepository accountRepository;
    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final SimplePlayerAuthenticationPlugin plugin;

    public LoginCommand(SimplePlayerAuthenticationPlugin plugin, AccountRepository accountRepository, AuthenticatedUserRepository authenticatedUserRepository) {
        this.accountRepository = accountRepository;
        this.authenticatedUserRepository = authenticatedUserRepository;
        this.plugin = plugin;
    }

    @Default
    @Syntax("Usage: /login")
    public void onLoginCommand(Player player) {
        Optional<Account> optionalAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalAccount.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please login before proceeding\nUsername: &c%PLAYERNAME%\n&7Usage: &c/login [password]&7").replaceAll("%PLAYERNAME%", player.getDisplayName()));
            ;
            return;
        }

        Account account = optionalAccount.get();
        LoginGui loginGui = new LoginGui(plugin, authenticatedUserRepository, account);
        loginGui.open(player);
    }
}
