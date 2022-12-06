package com.skywalx.simpleplayerauthentication.config;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessageConfiguration {
    private final FileConfiguration configurationFile;
    private final boolean usePlaceholderApi;

    public MessageConfiguration(FileConfiguration messagesConfigurationFile, boolean isPlaceholderApiEnabled) {
        this.configurationFile = messagesConfigurationFile;
        this.usePlaceholderApi = isPlaceholderApiEnabled;
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
        if (player != null) {
            String playerName = player.getDisplayName();
            String formattedMessage = getFormattedMessage(messageKey, playerName);
            if(usePlaceholderApi) {
                formattedMessage = PlaceholderAPI.setPlaceholders(player, formattedMessage);
            }
            player.sendMessage(formattedMessage);
        }
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


