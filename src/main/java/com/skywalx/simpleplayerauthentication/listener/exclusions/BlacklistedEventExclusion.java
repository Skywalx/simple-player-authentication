package com.skywalx.simpleplayerauthentication.listener.exclusions;

import org.bukkit.event.Event;

public interface BlacklistedEventExclusion {

    boolean isNotBlacklisted(Event event);

}
