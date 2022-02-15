package com.bitnet.paulo.papuafk.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.bitnet.paulo.papuafk.Main;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

import me.clip.placeholderapi.PlaceholderAPI;

public class PacketsUtils {
	
	private final Main plugin;
	
	public PacketsUtils(Main plugin) {
		this.plugin = plugin;
		this.hologram_ram = new HashMap<>();
		this.hologram_viewers = new HashMap<>();
		this.entity_ram = new HashMap<>();
		this.entity_viewers = new HashMap<>();
	}
	
	private final HashMap<Player, List<PacketContainer>> hologram_ram;
	private final HashMap<PacketContainer, List<Player>> hologram_viewers;
	
	public HashMap<Player, List<PacketContainer>> getHologram_ram() {
		return hologram_ram;
	}
	public HashMap<PacketContainer, List<Player>> getHologram_viewers() {
		return hologram_viewers;
	}
	
	
	private String setPlaceholders(String string, Player p) {
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
				return PlaceholderAPI.setPlaceholders(p, string);
			}
		}else {
			return string;
		}
		return string;
	}
	
	public void sendHologram(Player p, List<String> lines, Player viewer) {
		Location toCopy = p.getLocation();
		double xOffSet = 0.0;
		double yOffSet = 2.0;
		double zOffSet = 0.0;
		for(int i = 0; i < lines.size(); i++) {
			PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY); 
			packet.getModifier().writeDefaults();
			int id = (int) (Math.random() * Integer.MAX_VALUE);
			packet.getIntegers().write(0, id);
			packet.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
			Location position = toCopy.clone().add(xOffSet, yOffSet, zOffSet);
			position = position.add(0.0, -(0.30D * i), 0.0);
			packet.getDoubles().write(0, position.getX());
			packet.getDoubles().write(1, position.getY());
			packet.getDoubles().write(2, position.getZ());
			if(!this.getHologram_ram().containsKey(p)) {
				List<PacketContainer> list = new ArrayList<>();
				list.add(packet);
				this.getHologram_ram().put(p, list);
			}else {
				List<PacketContainer> list = this.getHologram_ram().get(p);
				if(!list.contains(packet)) {
					list.add(packet);
				}
				this.getHologram_ram().remove(p);
				this.getHologram_ram().put(p, list);
			}
			try {
				ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, packet);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
			PacketContainer metadata = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
			metadata.getModifier().writeDefaults();
			metadata.getIntegers().write(0, id);
			Optional<?> optional = Optional.of(WrappedChatComponent.fromChatMessage(ChatColor.translateAlternateColorCodes('&', this.setPlaceholders(lines.get(i), p)))[0].getHandle());
			dataWatcher.setObject(new WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true)), optional);
			dataWatcher.setObject(new WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
			WrappedDataWatcher.WrappedDataWatcherObject invisible = new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class));
			dataWatcher.setObject(invisible, (byte) 0x20);
			metadata.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
			try {
				ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, metadata);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!this.getHologram_viewers().containsKey(packet)) {
				List<Player> list = new ArrayList<>();
				list.add(viewer);
				this.getHologram_viewers().put(packet, list);
			}else {
				List<Player> list = this.getHologram_viewers().get(packet);
				if(!list.contains(viewer)) {
					list.add(viewer);
				}
				this.getHologram_viewers().remove(packet);
				this.getHologram_viewers().put(packet, list);
			}
		}
	}
	
	public void deleteHologram(Player p) {
		if(this.getHologram_ram().containsKey(p)) {
			for(PacketContainer packets : this.getHologram_ram().get(p)) {
				PacketContainer destroy = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
				destroy.getModifier().writeDefaults();
				int[] ids = {packets.getIntegers().read(0)};
				destroy.getIntegerArrays().write(0, ids);
				for(Player player : Bukkit.getOnlinePlayers()) {
					try {
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, destroy);
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				this.getHologram_ram().remove(p);
				this.getHologram_viewers().remove(packets);
			}
		}
	}
	
	private final HashMap<Player, List<PacketContainer>> entity_ram;
	private final HashMap<PacketContainer, List<Player>> entity_viewers;
	
	public HashMap<Player, List<PacketContainer>> getEntity_ram() {
		return entity_ram;
	}
	public HashMap<PacketContainer, List<Player>> getEntity_viewers() {
		return entity_viewers;
	}
	
	////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("deprecation")
	public void replcePlayerEntity(Player p, Player watcher) {
		Location location = p.getLocation();
		double xOffSet = 0.0, yOffSet = 0.0, zOffSet = 0.0;
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
		packet.getModifier().writeDefaults();
		int id = (int) (Math.random() * Integer.MAX_VALUE);
		packet.getIntegers().write(0, id);
		packet.getEntityTypeModifier().write(0, EntityType.VILLAGER);
		Location position = location.clone().add(xOffSet, yOffSet, zOffSet);
		packet.getDoubles().write(0, position.getX());
		packet.getDoubles().write(1, position.getY());
		packet.getDoubles().write(2, position.getZ());
		if(!(this.getEntity_ram().containsKey(p))) {
			List<PacketContainer> list = new ArrayList<>();
			list.add(packet);
			this.getEntity_ram().put(p, list);
		}else {
			List<PacketContainer> list = this.getEntity_ram().get(p);
			if(!(list.contains(packet))) {
				list.add(packet);
			}
			this.getEntity_ram().remove(p);
			this.getEntity_ram().put(p, list);
		}
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(watcher, packet);
			watcher.hidePlayer(p);
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!(this.getEntity_viewers().containsKey(packet))) {
			List<Player> list = new ArrayList<>();
			list.add(watcher);
			this.getEntity_viewers().put(packet, list);
		}else {
			List<Player> list = this.getEntity_viewers().get(packet);
			if(!(list.contains(watcher))) {
				list.add(watcher);
			}
			this.getEntity_viewers().remove(packet);
			this.getEntity_viewers().put(packet, list);
		}
		Bukkit.getScheduler().runTaskLater(plugin, ()->{
			if(this.getEntity_ram().containsKey(p)) {
				for(PacketContainer destroy : this.getEntity_ram().get(p)) {
					PacketContainer send = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
					int[] ids = {destroy.getIntegers().read(0)};
					send.getModifier().writeDefaults();
					send.getIntegerArrays().write(0, ids);
					try {
						ProtocolLibrary.getProtocolManager().sendServerPacket(p, send);
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, 5L);
	}
	
	@SuppressWarnings("deprecation")
	public void deleteEntityPlayer(Player p) {
		if(this.getEntity_ram().containsKey(p)) {
			for(PacketContainer packets : this.getEntity_ram().get(p)) {
				PacketContainer destroy = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
				destroy.getModifier().writeDefaults();
				int[] ids = {packets.getIntegers().read(0)};
				destroy.getIntegerArrays().write(0, ids);
				try {
					ProtocolLibrary.getProtocolManager().sendServerPacket(p, destroy);
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(this.getEntity_viewers().containsKey(packets)) {
					for(Player player : this.getEntity_viewers().get(packets)) {
						try {
							ProtocolLibrary.getProtocolManager().sendServerPacket(player, destroy);
							player.showPlayer(p);
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				this.getEntity_viewers().remove(packets);
				Bukkit.getScheduler().runTaskLater(plugin, ()->{
					this.getEntity_ram().remove(p);
				}, 20L);
			}
		}
	}
}
