package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.EventExecutor;

import javax.annotation.Nonnull;
import java.util.Optional;

public class BlacklistedEventExecutor implements EventExecutor {

    private final AuthenticatedUserRepository authenticatedUserRepository;
    private final AccountRepository accountRepository;

    public BlacklistedEventExecutor(AuthenticatedUserRepository authenticatedUserRepository, AccountRepository accountRepository) {
        this.authenticatedUserRepository = authenticatedUserRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void execute(@Nonnull Listener listener, @Nonnull Event event) {
        if (!(event instanceof PlayerEvent playerEvent)) {
            return;
        }

        if (!(playerEvent instanceof Cancellable cancellablePlayerEvent)) {
            return;
        }

        Player player = playerEvent.getPlayer();
        Optional<Account> optionalAccount = accountRepository.findByUuid(player.getUniqueId());
        if(optionalAccount.isEmpty()) {
            cancellablePlayerEvent.setCancelled(true);
            return;
        }

        if(authenticatedUserRepository.isAuthenticated(optionalAccount.get())) {
            return;
        }

        cancellablePlayerEvent.setCancelled(true);
    }
}
