package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.config.MessageConfiguration;
import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PlayerJoinListener implements Listener {

    private final AuthenticatedUserRepository authenticationRepository;
    private final AccountRepository accountRepository;
    private final MessageConfiguration messageConfiguration;

    public PlayerJoinListener(AuthenticatedUserRepository authenticationRepository, AccountRepository accountRepository, MessageConfiguration messageConfiguration) {
        this.authenticationRepository = authenticationRepository;
        this.accountRepository = accountRepository;
        this.messageConfiguration = messageConfiguration;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Optional<Account> optionalAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalAccount.isEmpty()) {
            messageConfiguration.send(MessageKey.REGISTER, player);
            return;
        }

        Account account = optionalAccount.get();
        if (authenticationRepository.isAuthenticated(account)) {
            return;
        }

        messageConfiguration.send(MessageKey.LOGIN, player);
    }

}
