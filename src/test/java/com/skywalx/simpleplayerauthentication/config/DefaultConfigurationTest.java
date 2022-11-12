package com.skywalx.simpleplayerauthentication.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.junit.jupiter.api.Test;
import java.util.logging.Logger;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DefaultConfigurationTest {

    private final Logger logger = mock(Logger.class);

    @Test
    void getBlacklistedEventsBeforeAuthentication_givenSomeEntriesInConfig_shouldReturnProperEventClasses() {
        FileConfiguration configMock = mock(FileConfiguration.class);
        when(configMock.getStringList("blacklisted-events-before-login")).thenReturn(List.of("PlayerMoveEvent", "PlayerDropItemEvent"));
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration(configMock, logger);

        List<Class<? extends PlayerEvent>> playerEventClasses = defaultConfiguration.getBlacklistedEventsBeforeAuthentication();

        assertThat(playerEventClasses).containsExactlyInAnyOrder(PlayerMoveEvent.class, PlayerDropItemEvent.class);
    }

    @Test
    void getBlacklistedEventsBeforeAuthentication_givenAllEntryInConfig_shouldReturnAllPlayerEventClasses() {
        FileConfiguration configMock = mock(FileConfiguration.class);
        when(configMock.getStringList("blacklisted-events-before-login")).thenReturn(List.of("PlayerMoveEvent", "ALL"));
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration(configMock, logger);
        Reflections reflections = new Reflections("org.bukkit.event.player");
        Set<Class<? extends PlayerEvent>> expectedPlayerEventClasses = reflections.getSubTypesOf(PlayerEvent.class);

        List<Class<? extends PlayerEvent>> playerEventClasses = defaultConfiguration.getBlacklistedEventsBeforeAuthentication();

        assertThat(playerEventClasses).containsExactlyInAnyOrderElementsOf(expectedPlayerEventClasses);
    }

    @Test
    void getBlacklistedEventsBeforeAuthentication_givenInvalidEntryInConfig_shouldReturnProperEventClassesAndSkipInvalid() {
        FileConfiguration configMock = mock(FileConfiguration.class);
        when(configMock.getStringList("blacklisted-events-before-login")).thenReturn(List.of("PlayerMoveEvent", "InvalidPlayerEvent", "PlayerDropItemEvent"));
        DefaultConfiguration defaultConfiguration = new DefaultConfiguration(configMock, logger);

        List<Class<? extends PlayerEvent>> playerEventClasses = defaultConfiguration.getBlacklistedEventsBeforeAuthentication();

        verify(logger).severe("Blacklisted player event with class name: InvalidPlayerEvent is invalid! See https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/player/PlayerEvent.html for all valid types.");
        assertThat(playerEventClasses).containsExactlyInAnyOrder(PlayerMoveEvent.class, PlayerDropItemEvent.class);
    }

}