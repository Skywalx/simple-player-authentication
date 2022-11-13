package com.skywalx.simpleplayerauthentication.gui;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UnregisterGui {

    private final SimplePlayerAuthenticationPlugin plugin;
    private final AccountRepository accountRepository;
    private final AuthenticatedUserRepository authenticationRepository;
    private final Account account;

    public UnregisterGui(SimplePlayerAuthenticationPlugin plugin, AccountRepository accountRepository, AuthenticatedUserRepository authenticationRepository, Account account) {
        this.plugin = plugin;
        this.accountRepository = accountRepository;
        this.authenticationRepository = authenticationRepository;
        this.account = account;
    }

    public void open(Player playerOpener) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Enter password")
                .text("Unregister")
                .onComplete((player, password) -> {
                    if (!account.doesPasswordMatch(password)) {
                        return AnvilGUI.Response.text("Incorrect!");
                    }

                    authenticationRepository.remove(account);
                    accountRepository.delete(account);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6The account has been successfully unregistered!"));
                    return AnvilGUI.Response.close();
                })
                .open(playerOpener);
    }

}
