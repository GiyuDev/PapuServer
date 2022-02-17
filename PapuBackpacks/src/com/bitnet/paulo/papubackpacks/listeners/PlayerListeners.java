package com.bitnet.paulo.papubackpacks.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.bitnet.paulo.papubackpacks.Main;

public class PlayerListeners implements Listener {
	
	private final Main plugin;
	public PlayerListeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		this.getPlugin().getManager().insertInventory(e.getPlayer());
	}

	public Main getPlugin() {
		return plugin;
	}
}
