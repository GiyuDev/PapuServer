package com.bitnet.paulo.papubackpacks.manager;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bitnet.paulo.papubackpacks.Main;
import com.bitnet.paulo.papubackpacks.database.Database;

public class BackpackManager {
	
	private final Database database;
	
	private final HashMap<Player, Inventory> inv_map;
	private final HashMap<Player, Boolean> isWatching;
	
	private final Main plugin;
	public BackpackManager(Main plugin, Database database) {
		this.plugin = plugin;
		this.inv_map = new HashMap<>();
		this.isWatching = new HashMap<>();
		this.database = database;
		Bukkit.getScheduler().runTaskTimer(this.getPlugin(), ()->{
			this.saveAllContents();
		}, 1L, 1200L);
	}
	
	public HashMap<Player, Inventory> getInv_map() {
		return inv_map;
	}
	
	public HashMap<Player, Boolean> getIsWatching() {
		return isWatching;
	}

	public Main getPlugin() {
		return plugin;
	}
	
	public Database getDatabase() {
		return database;
	}

	public void insertInventory(Player p) {
		if(!this.playerHasInventory(p)) {
			Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&3&oTu mochila"));
			this.inv_map.put(p, inv);
			if(!this.getDatabase().playerHasFile(p)) {
				this.getDatabase().createFilePlayer(p);
				Bukkit.getScheduler().runTaskLater(this.getPlugin(), ()->{
					YamlConfiguration config = this.getDatabase().getPlayerFileConfig(p);
					config.set("contents", "");
					try {
						config.save(this.getDatabase().getPlayerFile(p));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}, 5L);
			}
		}
	}
	
	public Inventory getPlayerInventory(Player p) {
		if(this.playerHasInventory(p)) {
			Inventory inv = this.getInv_map().get(p);
			return inv;
		}
		return null;
	}
	
	public boolean playerHasInventory(Player p) {
		return this.getInv_map().containsKey(p);
	}
	
	public void saveAllContents() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(this.playerHasInventory(p)) {
				for(Map.Entry<Player, Inventory> mapa : this.getInv_map().entrySet()) {
					if(this.getDatabase().playerHasFile(mapa.getKey())) {
						YamlConfiguration config = this.getDatabase().getPlayerFileConfig(mapa.getKey());
						if(config.contains("contents")) {
							Inventory inv = mapa.getValue();
							if(inv.getContents().length > 0) {
								List<ItemStack> list = Arrays.asList(inv.getContents());
								config.set("contents", list);
								try {
									config.save(this.getDatabase().getPlayerFile(p));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else {
								continue;
							}
						}
					}
				}
			}
		}
	}
}
