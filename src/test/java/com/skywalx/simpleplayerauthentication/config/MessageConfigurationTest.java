package com.skywalx.simpleplayerauthentication.config;

import com.skywalx.simpleplayerauthentication.config.MessageConfiguration.MessageKey;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.*;

class MessageConfigurationTest {

    private final MessageConfiguration messageConfiguration = new MessageConfiguration(YamlConfiguration.loadConfiguration(new File("src/test/resources/messages.yml")));

    @Test
    void send_shouldSendChatColorTranslated_toTargetPlayer() {
        Player player = mock(Player.class);
        when(player.getDisplayName()).thenReturn("HungryDev");

        messageConfiguration.send(MessageKey.ALREADY_REGISTERED, player);

        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe account for HungryDev is already registered!"));
    }

    @Test
    void send_givenNullPlayer_shouldSendChatColorTranslatedWithDefaultUnknown_toTargetPlayer() {
        Player player = mock(Player.class);

        messageConfiguration.send(MessageKey.LOGIN, player);

        verify(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please login before proceeding\nUsername: &cUnknown\n&7Usage: &c/login&7"));
    }

}