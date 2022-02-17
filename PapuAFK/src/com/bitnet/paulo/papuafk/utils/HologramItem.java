package com.bitnet.paulo.papuafk.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bitnet.paulo.papuafk.Main;
import com.bitnet.paulo.papuafk.player.AFKPlayer;

public class HologramItem {
	
	private final HashMap<Player, Item> player_item_cache;
	private final List<Item> item_cache;
	
	private final HashMap<Player, ArmorStand> player_armorstand_cache;
	private final List<ArmorStand> armor_stand_cache;
	
	private final Main plugin;
	
	public HologramItem(Main plugin) {
		this.plugin = plugin;
		this.player_item_cache = new HashMap<>();
		this.item_cache = new ArrayList<>();
		this.player_armorstand_cache = new HashMap<>();
		this.armor_stand_cache = new ArrayList<>();
		
		Bukkit.getScheduler().runTaskTimer(this.getPlugin(), ()->{
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
					AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(p);
					if(!afk.isAfk()) {
						if(this.getPlayer_armorstand_cache().containsKey(p)) {
							if(this.getPlayer_item_cache().containsKey(p)) {
								ArmorStand armor = this.getPlayer_armorstand_cache().get(p);
								Item item = this.getPlayer_item_cache().get(p);
								if(this.getArmor_stand_cache().contains(armor)) {
									this.getArmor_stand_cache().remove(armor);
									armor.removePassenger((Entity) item);
									armor.remove();
								}
								if(this.getItem_cache().contains(item)) {
									this.getItem_cache().remove(item);
									item.remove();
								}
							}
						}
					}
				}else {
					continue;
				}
			}
		}, 0L, 1200L);
	}
	
	@SuppressWarnings("deprecation")
	public void spawnItemPlayer(Player p) {
		if(!this.getPlayer_item_cache().containsKey(p)) {
			Location location = p.getLocation();
			Item item = location.getWorld().dropItemNaturally(location, new ItemStack(Material.BARRIER));
			item.setPickupDelay(926039576);
			item.setGravity(false);
			item.setCustomNameVisible(false);
			ArmorStand armor = location.getWorld().spawn(location.clone().add(0.0, 2.0, 0.0), ArmorStand.class);
			armor.setVisible(false);
			armor.setGravity(false);
			armor.setCustomNameVisible(false);
			armor.setSmall(true);
			armor.setMarker(false);
			armor.setPassenger((Entity) item);
			armor.setCustomNameVisible(false);
			if(!this.getPlayer_armorstand_cache().containsKey(p)) {
				this.getPlayer_armorstand_cache().put(p, armor);
			}
			if(!this.getPlayer_item_cache().containsKey(p)) {
				this.getPlayer_item_cache().put(p, item);
			}
			if(!this.getArmor_stand_cache().contains(armor)) {
				this.getArmor_stand_cache().add(armor);
			}
			if(!this.getItem_cache().contains(item)) {
				this.getItem_cache().add(item);
			}
		}
	}
	
	public void removeItemPlayer(Player p) {
		if(this.getPlayer_armorstand_cache().containsKey(p)) {
			if(this.getPlayer_item_cache().containsKey(p)) {
				ArmorStand armor = this.getPlayer_armorstand_cache().get(p);
				Item item = this.getPlayer_item_cache().get(p);
				if(this.getArmor_stand_cache().contains(armor)) {
					this.getArmor_stand_cache().remove(armor);
					armor.removePassenger((Entity) item);
					armor.remove();
				}
				if(this.getItem_cache().contains(item)) {
					this.getItem_cache().remove(item);
					item.remove();
				}
				Bukkit.getScheduler().runTaskLater(this.getPlugin(), ()->{
					this.getPlayer_armorstand_cache().remove(p);
					this.getPlayer_item_cache().remove(p);
				}, 5L);
			}
		}
	}

	public HashMap<Player, Item> getPlayer_item_cache() {
		return player_item_cache;
	}

	public List<Item> getItem_cache() {
		return item_cache;
	}

	public HashMap<Player, ArmorStand> getPlayer_armorstand_cache() {
		return player_armorstand_cache;
	}

	public List<ArmorStand> getArmor_stand_cache() {
		return armor_stand_cache;
	}

	public Main getPlugin() {
		return plugin;
	}
}
