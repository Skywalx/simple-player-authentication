package com.skywalx.simpleplayerauthentication.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerEvent;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DefaultConfiguration {

    private final FileConfiguration defaultConfiguration;
    private final Logger logger;

    public DefaultConfiguration(FileConfiguration defaultConfiguration, Logger logger) {
        this.defaultConfiguration = defaultConfiguration;
        this.logger = logger;
    }

    @SuppressWarnings("unchecked")
    public List<Class<? extends PlayerEvent>> getBlacklistedEventsBeforeAuthentication() {
        return defaultConfiguration.getStringList("blacklisted-events-before-login")
                .stream()
                .map(this::convertKeyToPlayerEventClasses)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(playerEventClass -> (Class<PlayerEvent>) playerEventClass)
                .distinct()
                .filter(playerEventClass -> !Modifier.isAbstract(playerEventClass.getModifiers()))
                .collect(Collectors.toList());
    }

    private Collection<? extends Class<?>> convertKeyToPlayerEventClasses(String playerEventClassName) {
        if (playerEventClassName.equalsIgnoreCase("ALL")) {
            Reflections reflections = new Reflections("org.bukkit.event.player");
            return reflections.getSubTypesOf(PlayerEvent.class);
        }

        try {
            return List.of(Class.forName("org.bukkit.event.player." + playerEventClassName));
        } catch (ClassNotFoundException e) {
            logger.severe("Blacklisted player event with class name: " + playerEventClassName + " is invalid! See https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/player/PlayerEvent.html for all valid types.");
            return null;
        }
    }
}
