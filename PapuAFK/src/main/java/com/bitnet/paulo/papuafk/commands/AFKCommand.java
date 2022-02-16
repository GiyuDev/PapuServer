package com.bitnet.paulo.papuafk.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bitnet.paulo.papuafk.Main;
import org.jetbrains.annotations.NotNull;

public class AFKCommand implements CommandExecutor {

    private final Main plugin;

    public AFKCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // TODO Auto-generated method stub
        if (!(sender instanceof Player)) {
            return false;
        } else {
            Player p = (Player) sender;
            if (args.length < 1) {
                if (p.isOp()) {
                    if (this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
                        if (!this.getPlugin().getAfkManager().getAfk_list().contains(p)) {
                            this.getPlugin().getAfkManager().actionsAFK(p);
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lEY! hands up nigga, you can't do that, open fire this man is black!!"));
                }
            } else {
                if (p.isOp()) {
                    switch (args[0]) {
                        case "setafklocation":
                            if (this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
                                this.getPlugin().getDatabase().updateAFKLocationDatabase(p);
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fUbicacion guardada en: " + "&c" + this.getPlugin().getDatabase().getAFKLocationDatabase(p)));
                            }
                            break;
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lEY! hands up nigga, you can't do that, open fire this man is black!!"));
                }
            }
        }
        return true;
    }

    public Main getPlugin() {
        return plugin;
    }
}
