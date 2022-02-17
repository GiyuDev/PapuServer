package com.bitnet.paulo.papuafk.listeners;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.bitnet.paulo.papuafk.Main;
import com.bitnet.paulo.papuafk.player.AFKPlayer;

public class PlayerListeners implements Listener {
	
	private final HashMap<Player, Boolean> deathMap_packets;
	
	private final Main plugin;
	public PlayerListeners(Main plugin) {
		this.plugin = plugin;
		this.deathMap_packets = new HashMap<>();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void entrar(PlayerJoinEvent e) {
		this.getPlugin().getAfkManager().createAFKPlayer(e.getPlayer());
		this.getPlugin().getDatabase().createPlayer(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void salir(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			Bukkit.getScheduler().runTaskLater(plugin, ()->{
				this.getPlugin().getAfkManager().getAfk_map().remove(p);
			}, 1L);
			if(this.getPlugin().getAfkManager().getAfk_list().contains(p)) {
				this.getPlugin().getAfkManager().actionsUnAFK(p);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void move(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(p);
			afk.setPlayTime(0);
			if(this.getPlugin().getAfkManager().getAfk_list().contains(p) || this.getPlugin().getAfkManager().getAFKPlayer(p).getPlayTime() >= 60 || this.getPlugin().getAfkManager().getAFKPlayer(p).isAfk()) {
				this.getPlugin().getAfkManager().actionsUnAFK(p);
			}
		}
	}
	
	@EventHandler
	public void chat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(p);
			if(afk.isAfk()) {
				this.getPlugin().getAfkManager().actionsUnAFK(p);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void joinPackets(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(this.getPlugin().getAfkManager().containsAFKPlayer(player)) {
				AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(player);
				if(afk.isAfk()) {
					if(this.getPlugin().isProtocolHook()) {
						this.getPlugin().getPackets().sendHologram(player, Arrays.asList("&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>", "&fEste pedazo de homosexual: &e%player_name%", "&fEsta AFK! pero ya va a regresar", "&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>", "&f"), p);
						player.hidePlayer(p);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void leave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(p);
			if(afk.isAfk()) {
				this.getPlugin().getAfkManager().actionsUnAFK(p);
			}
		}
	}
	
	@EventHandler
	public void cancelDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
				AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(p);
				if(afk.isAfk()) {
					e.setCancelled(true);
				}else {
					e.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler
	public void cancelFoodLevelChange(FoodLevelChangeEvent e) {
		Player p = (Player) e.getEntity();
		if(this.getPlugin().getAfkManager().containsAFKPlayer(p)) {
			AFKPlayer player = this.getPlugin().getAfkManager().getAFKPlayer(p);
			if(player.isAfk()) {
				e.setCancelled(true);
			}else {
				e.setCancelled(false);
			}
		}
	}
	
	@EventHandler
	public void death(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(!this.getDeathMap_packets().containsKey(p)) {
			this.getDeathMap_packets().put(p, true);
		}
	}
	
	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(this.getDeathMap_packets().containsKey(p)) {
			Boolean evaluate = this.getDeathMap_packets().get(p);
			if(evaluate) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					AFKPlayer afk = this.getPlugin().getAfkManager().getAFKPlayer(player);
					if(afk.isAfk()) {
						new BukkitRunnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								getPlugin().getPackets().sendHologram(player, Arrays.asList("&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>", "&fEste pedazo de homosexual: &e%player_name%", "&fEsta AFK! pero ya va a regresar", "&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>", "&f"), p);
							}
							
						}.runTaskLater(this.getPlugin(), 5L);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void checkinvView(InventoryOpenEvent e) {
		if(e.getView().getTopInventory().getType() == InventoryType.WORKBENCH || e.getView().getTopInventory().getType() == InventoryType.ENCHANTING ||
				e.getView().getTopInventory().getType() == InventoryType.ANVIL || e.getView().getTopInventory().getType() == InventoryType.FURNACE || e.getView().getTopInventory().getType() == InventoryType.CHEST
				|| e.getView().getTopInventory().getType() == InventoryType.ENDER_CHEST) {
			Player p = (Player) e.getPlayer();
			if(this.getPlugin().getAfkManager().getInv_view().containsKey(p)) {
				boolean evaluate = this.getPlugin().getAfkManager().getInv_view().get(p);
				if(!evaluate) {
					this.getPlugin().getAfkManager().getInv_view().put(p, true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void closeInv(InventoryCloseEvent e) {
		if(e.getView().getTopInventory().getType() == InventoryType.WORKBENCH || e.getView().getTopInventory().getType() == InventoryType.ENCHANTING ||
				e.getView().getTopInventory().getType() == InventoryType.ANVIL || e.getView().getTopInventory().getType() == InventoryType.FURNACE || e.getView().getTopInventory().getType() == InventoryType.CHEST
				|| e.getView().getTopInventory().getType() == InventoryType.ENDER_CHEST) {
			Player p = (Player) e.getPlayer();
			if(this.getPlugin().getAfkManager().getInv_view().containsKey(p)) {
				boolean evaluate = this.getPlugin().getAfkManager().getInv_view().get(p);
				if(evaluate) {
					this.getPlugin().getAfkManager().getInv_view().put(p, false);
				}
			}
		}
	}

	public Main getPlugin() {
		return plugin;
	}

	public HashMap<Player, Boolean> getDeathMap_packets() {
		return deathMap_packets;
	}
	
}
