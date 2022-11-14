package com.skywalx.simpleplayerauthentication.gui;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public class UnregisterGui {

    private final SimplePlayerAuthenticationPlugin plugin;
    private final AccountRepository accountRepository;
    private final AuthenticatedUserRepository authenticationRepository;
    private final Account account;
    private final MessageConfiguration messagesConfiguration;

    public UnregisterGui(SimplePlayerAuthenticationPlugin plugin, AccountRepository accountRepository, AuthenticatedUserRepository authenticationRepository, Account account, MessageConfiguration messagesConfiguration) {
        this.plugin = plugin;
        this.accountRepository = accountRepository;
        this.authenticationRepository = authenticationRepository;
        this.account = account;
        this.messagesConfiguration = messagesConfiguration;
    }

    public void open(Player playerOpener) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Enter password")
                .text("Unregister")
                .onComplete((player, password) -> {
                    if (!account.doesPasswordMatch(password)) {
                        return AnvilGUI.Response.text(messagesConfiguration.getFormattedMessage(MessageKey.NOT_MATCHING_PASSWORD));
                    }

                    authenticationRepository.remove(account);
                    accountRepository.delete(account);
                    messagesConfiguration.send(MessageKey.SUCCESSFUL_UNREGISTRATION, player);
                    return AnvilGUI.Response.close();
                })
                .open(playerOpener);
    }

}
