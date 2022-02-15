package com.bitnet.paulo.papuafk.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.bitnet.paulo.papuafk.Main;
import com.bitnet.paulo.papuafk.player.AFKPlayer;

public class PlayerListeners implements Listener {
	
	private final Main plugin;
	public PlayerListeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void entrar(PlayerJoinEvent e) {
		this.getPlugin().getAfkManager().createAFKPlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void move(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			if(this.getPlugin().getAfkManager().getAfk_list().contains(this.getPlugin().getAfkManager().getAFKPlayer(p).getName())) {
				this.getPlugin().getAfkManager().actionsUnAFK(p);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void reiniciar(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(p);
			afk.setPlayTime(0);
			afk.setAfk(false);
			afk.setLoc(this.getPlugin().getAfkManager().getAFKPlayer(p).getLoc());
			afk.setName(this.getPlugin().getAfkManager().getAFKPlayer(p).getName());
			this.getPlugin().getAfkManager().getAfk_map().put(p, afk);
		}
	}

	public Main getPlugin() {
		return plugin;
	}
}
