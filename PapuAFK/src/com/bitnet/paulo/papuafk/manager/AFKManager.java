package com.bitnet.paulo.papuafk.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bitnet.paulo.papuafk.Main;
import com.bitnet.paulo.papuafk.player.AFKPlayer;

public class AFKManager {
	
	private ArrayList<Player> afk_list;
	private HashMap<Player, AFKPlayer> afk_map;
	
	private final Main plugin;
	public AFKManager(Main plugin) {
		this.plugin = plugin;
		this.afk_list = new ArrayList<>();
		this.afk_map = new HashMap<>();
	}
	
	public ArrayList<Player> getAfk_list() {
		return afk_list;
	}
	public HashMap<Player, AFKPlayer> getAfk_map() {
		return afk_map;
	}

	public Main getPlugin() {
		return plugin;
	}
	
	public void createAFKPlayer(Player p) {
		if(!this.containsAFKPlayer(p)) {
			AFKPlayer afk = new AFKPlayer();
			afk.setName(p.getName());
			afk.setAfk(false);
			afk.setLoc(null);
			afk.setPlayTime(0);
			afk_map.put(p, afk);
		}
	}
	
	public boolean containsAFKPlayer(Player p) {
		return this.getAfk_map().containsKey(p);
	}
	
	public AFKPlayer getAFKPlayer(Player p) {
		if(this.containsAFKPlayer(p)) {
			return this.getAfk_map().get(p);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void actionsAFK(Player p) {
		if(!this.getAFKPlayer(p).isAfk()) {
			if(!this.getAfk_list().contains(p)) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					AFKPlayer afk = this.getAFKPlayer(p);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + " &fEsta AFK :v"));
					p.playSound(p.getLocation(), Sound.ENTITY_BAT_HURT, 1.0f, 1.0f);
					p.hidePlayer(player);
					afk.setAfk(true);
					if(this.getPlugin().getDatabase().playerHasFile(p)) {
						this.getPlugin().getDatabase().updateLastLocationDatabase(p);
						this.getPlugin().getDatabase().updateAFKInventoryDatabase(p);
						this.getPlugin().getDatabase().updateAFKArmorDatabase(p);
						afk.setLoc(this.getPlugin().getDatabase().getLastLocationDatabase(p));
						YamlConfiguration config = this.getPlugin().getDatabase().getPlayerFileConfig(p);
						if(config.contains("location_afk")) {
							p.teleport(this.getPlugin().getDatabase().getAFKLocationDatabase(p));
						}
						Bukkit.getScheduler().runTaskLater(plugin, ()->{
							ItemStack[] nulled = {new ItemStack(Material.AIR)};
							p.getInventory().setContents(nulled);
							p.getInventory().setHelmet(new ItemStack(Material.AIR));
							p.getInventory().setChestplate(new ItemStack(Material.AIR));
							p.getInventory().setLeggings(new ItemStack(Material.AIR));
							p.getInventory().setBoots(new ItemStack(Material.AIR));
							if(this.getPlugin().isProtocolHook()) {
								this.getPlugin().getPackets().sendHologram(p, Arrays.asList("&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>", "&fEste pedazo de homosexual: &e%player_name%", "&fEsta AFK! pero ya va a regresar", "&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>"), player);
							}
						}, 20L);
					}
					if(!this.afk_list.contains(player)) {
						this.afk_list.add(player);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void actionsUnAFK(Player p) {
		if(this.getAFKPlayer(p).isAfk()) {
			if(this.getAfk_list().contains(p)) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					AFKPlayer afk = this.getAFKPlayer(p);
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + p.getName() + " &fRevivio :D"));
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
					p.showPlayer(player);
					afk.setAfk(false);
					if(this.getPlugin().getDatabase().playerHasFile(p)) {
						if(afk.getLoc() != null) {
							p.teleport(this.getPlugin().getDatabase().getLastLocationDatabase(p));
						}
						Bukkit.getScheduler().runTaskLater(plugin, ()-> {
							if(!(this.getPlugin().getDatabase().getAFKInventoryDatabase(p).length == 0)) {
								p.getInventory().setContents(this.getPlugin().getDatabase().getAFKInventoryDatabase(p));
							}
							if(!(this.getPlugin().getDatabase().getAFKArmorDatabase(p).length == 0)) {
								p.getInventory().setArmorContents(this.getPlugin().getDatabase().getAFKArmorDatabase(p));
							}
							if(this.getPlugin().isProtocolHook()) {
								this.getPlugin().getPackets().deleteHologram(p);						
							}
						}, 20L);
					}
					afk.setPlayTime(0);
					if(this.afk_list.contains(p)) {
						this.afk_list.remove(p);
					}
				}
			}
		}
	}
}
