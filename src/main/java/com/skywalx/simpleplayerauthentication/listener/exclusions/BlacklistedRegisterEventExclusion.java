package com.skywalx.simpleplayerauthentication.listener.exclusions;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class BlacklistedRegisterEventExclusion implements BlacklistedEventExclusion {

    @Override
    public boolean isNotBlacklisted(Event event) {
        if(!(event instanceof PlayerCommandPreprocessEvent commandEvent)) {
            return false;
        }

        return commandEvent.getMessage().startsWith("/register");
    }
}
