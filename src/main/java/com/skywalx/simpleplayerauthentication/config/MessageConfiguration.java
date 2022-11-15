package com.skywalx.simpleplayerauthentication.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageConfiguration {
    private final FileConfiguration configurationFile;

    public MessageConfiguration(FileConfiguration messagesConfigurationFile) {
        this.configurationFile = messagesConfigurationFile;
    }

    public String getFormattedMessage(MessageKey messageKey) {
        return getFormattedMessage(messageKey, null);
    }

    public String getFormattedMessage(MessageKey messageKey, String playerDisplayName) {
        String playerName = "Unknown";
        if (playerDisplayName != null) {
            playerName = playerDisplayName;
        }
        String rawMessage = configurationFile.getString(messageKey.getConfigKey());
        String translateAlternateColorCodes = ChatColor.translateAlternateColorCodes('&', rawMessage);
        return translateAlternateColorCodes.replaceAll("%PLAYERNAME%", playerName);
    }

    public void send(MessageKey messageKey, Player player) {
        String playerName = "Unknown";
        if (player != null) {
            playerName = player.getDisplayName();
        }
        player.sendMessage(getFormattedMessage(messageKey, playerName));
    }

    public enum MessageKey {
        REGISTER("register"),
        LOGIN("login"),
        ALREADY_REGISTERED("already-registered"),
        ALREADY_LOGGED_IN("already-logged-in"),
        NOT_MATCHING_PASSWORD("not-matching-password"),
        WRONG_PASSWORD("wrong-password"),
        SUCCESSFUL_REGISTRATION("successful-registration"),
        SUCCESSFUL_LOGIN("successful-login"),
        SUCCESSFUL_UNREGISTRATION("successful-unregistration"),
        SUCCESSFUL_LOGOUT("successful-logout");

        private final String configKey;

        MessageKey(String configKey) {
            this.configKey = configKey;
        }

        public String getConfigKey() {
            return configKey;
        }
    }

}


