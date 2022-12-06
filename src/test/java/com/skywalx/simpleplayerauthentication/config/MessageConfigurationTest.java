package com.skywalx.simpleplayerauthentication.config;

import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.manager.LocalExpansionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.File;

import static org.mockito.Mockito.*;

class MessageConfigurationTest {

    @Test
    void send_shouldSendChatColorTranslated_toTargetPlayer() {
        MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);
        Player player = mock(Player.class);
        when(player.getDisplayName()).thenReturn("HungryDev");

        messageConfiguration.send(MessageKey.ALREADY_REGISTERED, player);

        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe account for HungryDev is already registered!"));
    }

    @Test
    void send_givenNullPlayer_shouldSendChatColorTranslatedWithDefaultUnknown_toTargetPlayer() {
        MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);
        Player player = mock(Player.class);

        messageConfiguration.send(MessageKey.LOGIN, player);

        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please login before proceeding\nUsername: &cUnknown\n&7Usage: &c/login&7"));
    }

    @Test
    void send_givenPlaceholderInMessage_shouldTranslatePlaceholderToValue() {
        MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), true);
        Player player = mock(Player.class);
        String formattedMessage = messageConfiguration.getFormattedMessage(MessageKey.LOGIN, player.getDisplayName());
        try (MockedStatic<PlaceholderAPI> placeholderMock = Mockito.mockStatic(PlaceholderAPI.class)) {
            placeholderMock.when(() -> PlaceholderAPI.setPlaceholders(any(), anyString()))
                    .thenAnswer((Answer<Void>) invocation -> null);

            messageConfiguration.send(MessageKey.LOGIN, player);

            placeholderMock.verify(() -> PlaceholderAPI.setPlaceholders(eq(player), eq(formattedMessage)));
        }
    }

    @Test
    void send_withNoPlaceholderApiPresent_shouldNotSetPlaceholders() {
        MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")), false);
        Player player = mock(Player.class);
        try (MockedStatic<PlaceholderAPI> placeholderMock = Mockito.mockStatic(PlaceholderAPI.class)) {

            messageConfiguration.send(MessageKey.LOGIN, player);

            placeholderMock.verifyNoInteractions();
        }
    }

}