package com.skywalx.simpleplayerauthentication.gui;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public class LoginGui {

    private final SimplePlayerAuthenticationPlugin plugin;
    private final AuthenticatedUserRepository authenticationRepository;
    private final Account account;
    private final MessageConfiguration messageConfiguration;

    public LoginGui(SimplePlayerAuthenticationPlugin plugin, AuthenticatedUserRepository authenticationRepository, Account account, MessageConfiguration messageConfiguration) {
        this.plugin = plugin;
        this.authenticationRepository = authenticationRepository;
        this.account = account;
        this.messageConfiguration = messageConfiguration;
    }

    public void open(Player playerOpener) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Enter password")
                .text("Login")
                .onComplete((player, password) -> {
                    if (!account.doesPasswordMatch(password)) {
                        return AnvilGUI.Response.text(messageConfiguration.getFormattedMessage(MessageKey.WRONG_PASSWORD));
                    }

                    authenticationRepository.add(account);
                    messageConfiguration.send(MessageKey.SUCCESSFUL_LOGIN, player);
                    return AnvilGUI.Response.close();
                })
                .open(playerOpener);
    }

}
