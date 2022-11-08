package com.skywalx.simpleplayerauthentication.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
//        Player player = playerMoveEvent.getPlayer();
//        Location currentLocation = player.getLocation();
//        player.teleport(currentLocation);
    }
}
