package com.bitnet.paulo.papuafk.task;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.bitnet.paulo.papuafk.Main;
import com.bitnet.paulo.papuafk.player.AFKPlayer;

import me.clip.placeholderapi.PlaceholderAPI;

public class AFKTask implements Runnable {

    private final Main plugin;

    public AFKTask(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
                for (Map.Entry<Player, AFKPlayer> mapa : this.getPlugin().getAfkManager().getAfk_map().entrySet()) {
                    if (!this.getPlugin().getAfkManager().getAfk_list().contains(mapa.getKey())) {
                        Player player = mapa.getKey();
                        int current = mapa.getValue().getPlayTime();
                        int new_value = current + 1;
                        mapa.getValue().setPlayTime(new_value);
                        String msg = "";
                        switch (mapa.getValue().getPlayTime()) {
                            case 30:
                                msg = "&e%player_name% &fbro? estas vivo?";
                                if (this.getPlugin().isPapihook()) {
                                    msg = PlaceholderAPI.setPlaceholders(player, msg);
                                }
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                player.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                                break;
                            case 45:
                                msg = "&e%player_name% &fNo pos creo que esta muerto :v";
                                if (this.getPlugin().isPapihook()) {
                                    msg = PlaceholderAPI.setPlaceholders(player, msg);
                                }
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                                break;
                            case 55:
                                msg = "&e%player_name% &fnow suck that cock, cock, cock, cock, cock, cock, now shake that ass.";
                                if (this.getPlugin().isPapihook()) {
                                    msg = PlaceholderAPI.setPlaceholders(p, msg);
                                }
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
                                break;
                            case 60:
                                this.getPlugin().getAfkManager().actionsAFK(player);
                                break;
                        }
                    }
                }
            }
        }
    }

    public Main getPlugin() {
        return plugin;
    }
}
