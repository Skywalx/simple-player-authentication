package com.skywalx.simpleplayerauthentication.gui;

import com.skywalx.simpleplayerauthentication.SimplePlayerAuthenticationPlugin;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

public class RegistrationGui {

    private final SimplePlayerAuthenticationPlugin plugin;
    private final AccountRepository accountRepository;
    private final HashingService hashingService;

    public RegistrationGui(SimplePlayerAuthenticationPlugin plugin, AccountRepository accountRepository, HashingService hashingService) {
        this.plugin = plugin;
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
    }

    public void open(Player playerOpener) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Choose a password")
                .text("enter")
                .onComplete((player, password) -> {
                    Confirmation confirmation = new Confirmation(password);
                    confirmation.open(playerOpener);
                    return AnvilGUI.Response.close();
                })
                .open(playerOpener);
    }

    private ItemStack closeItem() {
        ItemStack closeItem = new ItemStack(Material.BARRIER);

        ItemMeta closeItemMeta = closeItem.getItemMeta();
        closeItemMeta.setDisplayName("§cClose");
        closeItem.setItemMeta(closeItemMeta);
        return closeItem;
    }

    private ItemStack confirmationItem() {
        ItemStack accept = new ItemStack(Material.GREEN_TERRACOTTA);
        ItemMeta itemMeta = accept.getItemMeta();
        itemMeta.setDisplayName("§aAccept");
        accept.setItemMeta(itemMeta);
        return accept;
    }

    class Confirmation {

        private final String password;

        public Confirmation(String password) {
            this.password = password;
        }

        public void open(Player playerOpener) {
            new AnvilGUI.Builder()
                    .plugin(plugin)
                    .title("Retype password")
                    .text("enter")
                    .onComplete((player, passwordConfirmation) -> {
                        if (!password.equals(passwordConfirmation)) {
                            return AnvilGUI.Response.text("Not equal!");
                        }

                        Account account = new Account(player.getUniqueId(), password, hashingService);
                        try {
                            accountRepository.save(account);
                            player.sendMessage("§6You have successfully registered " + player.getDisplayName() + "!");
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            return AnvilGUI.Response.text("Failed to write to database!");
                        }
                        return AnvilGUI.Response.close();
                    })
                    .open(playerOpener);
        }
    }

}
