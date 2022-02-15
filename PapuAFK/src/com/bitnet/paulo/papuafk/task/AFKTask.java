package com.bitnet.paulo.papuafk.task;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bitnet.paulo.papuafk.Main;
import com.bitnet.paulo.papuafk.player.AFKPlayer;

public class AFKTask implements Runnable {

	private final Main plugin;
	public AFKTask(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
				for(Map.Entry<Player, AFKPlayer> mapa : this.getPlugin().getAfkManager().getAfk_map().entrySet()) {
					Player player = mapa.getKey();
					int current = mapa.getValue().getPlayTime();
					int new_value = current + 1;
					mapa.getValue().setPlayTime(new_value);
					if(mapa.getValue().getPlayTime() == 60) {
						this.getPlugin().getAfkManager().actionsAFK(player);
					}
				}
			}
		}
	}

	public Main getPlugin() {
		return plugin;
	}
}
