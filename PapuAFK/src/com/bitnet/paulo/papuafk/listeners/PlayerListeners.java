package com.bitnet.paulo.papuafk.listeners;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
			AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(p);
			afk.setPlayTime(0);
			if(this.getPlugin().getAfkManager().getAfk_list().contains(p)) {
				this.getPlugin().getAfkManager().actionsUnAFK(p);
			}
		}
	}
	
	@EventHandler
	public void joinHologram(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		for(Player player : this.getPlugin().getAfkManager().getAfk_list()) {
			if(this.getPlugin().isProtocolHook()) {
				this.getPlugin().getPackets().sendHologram(player, Arrays.asList("&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>", "&fEste pedazo de homosexual: &e%player_name%", "&fEsta AFK! pero ya va a regresar", "&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>"), p);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void leave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			if(this.getPlugin().getAfkManager().getAfk_list().contains(p)) {
				this.getPlugin().getAfkManager().actionsUnAFK(p);
			}
		}
	}

	public Main getPlugin() {
		return plugin;
	}
}
