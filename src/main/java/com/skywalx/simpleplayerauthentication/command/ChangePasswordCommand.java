package com.skywalx.simpleplayerauthentication.command;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Syntax;
import com.skywalx.simpleplayerauthentication.service.AccountRepository;
import com.skywalx.simpleplayerauthentication.service.HashingService;
import com.skywalx.simpleplayerauthentication.service.model.Account;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Optional;

@CommandAlias("changepassword")
@Description("Command that changes the password of a registered account.")
public class ChangePasswordCommand {

    private final AccountRepository accountRepository;
    private final HashingService hashingService;

    public ChangePasswordCommand(AccountRepository accountRepository, HashingService hashingService) {
        this.accountRepository = accountRepository;
        this.hashingService = hashingService;
    }

    @Default
    @Syntax("Usage: /changepassword [current password] [new password] [new password]")
    public void onChangePasswordCommand(Player player, String currentPassword, String newPassword, String confirmationNewPassword) {
        Optional<Account> potentialAccount = accountRepository.findByUuid(player.getUniqueId());
        if (potentialAccount.isEmpty()) {
            player.sendMessage("This account doesn't exist");
            return;
        }

        Account currentAccount = potentialAccount.get();

        if (!currentAccount.doesPasswordMatch(currentPassword)) {
            player.sendMessage("The current password is incorrect!");
            return;
        }

        if (!newPassword.equals(confirmationNewPassword)) {
            player.sendMessage("The two new passwords do not match!");
            return;
        }

        Account newAccount = new Account(currentAccount.uuid(), newPassword, hashingService);
        try {
            accountRepository.delete(currentAccount);
            accountRepository.save(newAccount);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
