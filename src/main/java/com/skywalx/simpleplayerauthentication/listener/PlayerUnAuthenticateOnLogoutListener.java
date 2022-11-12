package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerUnAuthenticateOnLogoutListener implements Listener {

    private final AuthenticatedUserRepository authenticatedUserRepository;

    public PlayerUnAuthenticateOnLogoutListener(AuthenticatedUserRepository authenticatedUserRepository) {
        this.authenticatedUserRepository = authenticatedUserRepository;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        authenticatedUserRepository.remove(new Account(event.getPlayer().getUniqueId(), ""));
    }

}
