package com.skywalx.simpleplayerauthentication.event;

import org.bukkit.event.Event;

public interface BlacklistedEventExclusion {

    boolean isNotBlacklisted(Event event);

}
