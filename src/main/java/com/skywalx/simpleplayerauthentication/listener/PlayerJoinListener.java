package com.skywalx.simpleplayerauthentication.listener;

import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.AuthenticatedUserRepository;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PlayerJoinListener implements Listener {

    private final AuthenticatedUserRepository authenticationRepository;
    private final AccountRepository accountRepository;

    public PlayerJoinListener(AuthenticatedUserRepository authenticationRepository, AccountRepository accountRepository) {
        this.authenticationRepository = authenticationRepository;
        this.accountRepository = accountRepository;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Optional<Account> optionalAccount = accountRepository.findByUuid(player.getUniqueId());
        if (optionalAccount.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please register before proceeding\\nUsername: &c%PLAYERNAME%\\n&7Usage: &c/register [password] [password]&7"));
            return;
        }

        Account account = optionalAccount.get();
        if (authenticationRepository.isAuthenticated(account)) {
            return;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Please login before proceeding\\nUsername: &c%PLAYERNAME%\\n&7Usage: &c/login [password]&7"));
    }

}
