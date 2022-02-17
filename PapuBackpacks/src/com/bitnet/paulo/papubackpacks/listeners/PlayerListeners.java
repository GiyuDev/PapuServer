package com.bitnet.paulo.papubackpacks.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import com.bitnet.paulo.papubackpacks.Main;

public class PlayerListeners implements Listener {
	
	private final Main plugin;
	public PlayerListeners(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		this.getPlugin().getManager().insertInventory(p);
		Bukkit.getScheduler().runTaskLater(this.getPlugin(), ()->{
			if(this.getPlugin().getManager().playerHasInventory(p)) {
				Inventory inv = this.getPlugin().getManager().getPlayerInventory(p);
				inv.setContents(this.getPlugin().getManager().getPlayerContents(p));
			}
		}, 5L);
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent e) {
		this.getPlugin().getManager().saveContents(e.getPlayer());
	}
	
	@EventHandler
	public void openSound(InventoryOpenEvent e) {
		if(e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3&oTu mochila"))) {
			Player p = (Player) e.getPlayer();
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 2.0f);
		}
	}
	
	@EventHandler
	public void closeSound(InventoryCloseEvent e) {
		if(e.getView().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&3&oTu mochila"))) {
			Player p = (Player) e.getPlayer();
			p.playSound(p.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0f, 2.0f);
		}
	}

	public Main getPlugin() {
		return plugin;
	}
}
