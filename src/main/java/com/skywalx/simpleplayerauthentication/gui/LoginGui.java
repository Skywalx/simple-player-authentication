package com.skywalx.simpleplayerauthentication.gui;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LoginGui {

    private final SimplePlayerAuthenticationPlugin plugin;
    private final AuthenticatedUserRepository authenticationRepository;
    private final Account account;

    public LoginGui(SimplePlayerAuthenticationPlugin plugin, AuthenticatedUserRepository authenticationRepository, Account account) {
        this.plugin = plugin;
        this.authenticationRepository = authenticationRepository;
        this.account = account;
    }

    public void open(Player playerOpener) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Enter password")
                .text("enter")
                .onComplete((player, password) -> {
                    if (!account.doesPasswordMatch(password)) {
                        return AnvilGUI.Response.text("Incorrect!");
                    }

                    authenticationRepository.add(account);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You successfully logged in!"));
                    return AnvilGUI.Response.close();
                })
                .open(playerOpener);
    }

}
