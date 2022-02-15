package com.bitnet.paulo.papuafk.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.bitnet.paulo.papuafk.Main;

public class Database {
	
	private final Main plugin;
	private final File directorio;
	
	public Database(Main plugin) {
		this.plugin = plugin;
		this.directorio = new File(this.plugin.getDataFolder().getAbsolutePath(), "players");
		if(!(this.directorio.exists())) {
			if(this.directorio.mkdir()) {
				// NONE
			}
		}
	}
	
	/*
	 * SET METHODS
	 */
	
	////////////////////////////////////////////////////////////////////////////////
	
	public void createPlayer(Player p) {
		if(!this.playerHasFile(p)) {
			this.createFilePlayer(p);
			Bukkit.getScheduler().runTaskLater(plugin, ()->{
				YamlConfiguration config = this.getPlayerFileConfig(p);
				config.set("last_location", "");
				config.set("inventory", new ArrayList<>());
				config.set("armor", new ArrayList<>());
				config.set("location_afk", "");
				try {
					config.save(this.getPlayerFile(p));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}, 5L);
		}
	}
	
	public void updateLastLocationDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("last_location")) {
				config.set("last_location", plugin.locToString(p.getLocation()));
				try {
					config.save(this.getPlayerFile(p));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateAFKLocationDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("location_afk")) {
				config.set("location_afk", plugin.locToString(p.getLocation()));
				try {
					config.save(this.getPlayerFile(p));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateAFKInventoryDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("inventory")) {
				config.set("inventory", p.getInventory().getContents());
				try {
					config.save(this.getPlayerFile(p));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void updateAFKArmorDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("armor")) {
				config.set("armor", p.getInventory().getArmorContents());
				try {
					config.save(this.getPlayerFile(p));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	 * GET METHODS
	 */
	
	////////////////////////////////////////////////////////////////////////////////
	
	public Location getLastLocationDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("last_location")) {
				if(config.getString("last_location") != null) {
					return plugin.getLocationFromString(config.getString("last_location"));
				}else {
					return null;
				}
			}
		}else {
			return null;
		}
		return null;
	}
	
	public Location getAFKLocationDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("location_afk")) {
				if(config.getString("location_afk") != null) {
					return plugin.getLocationFromString(config.getString("location_afk"));
				}else {
					return null;
				}
			}
		}else {
			return null;
		}
		return null;
	}
	
	public ItemStack[] getAFKInventoryDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("inventory")) {
				if(!config.getList("inventory").isEmpty()) {
					return config.getList("inventory").stream().map(i -> (ItemStack) i).toArray(ItemStack[]::new);
				}else {
					return null;
				}
			}
		}else {
			return null;
		}
		return null;
	}
	
	public ItemStack[] getAFKArmorDatabase(Player p) {
		if(this.playerHasFile(p)) {
			YamlConfiguration config = this.getPlayerFileConfig(p);
			if(config.contains("armor")) {
				if(!config.getList("armor").isEmpty()) {
					return config.getList("armor").stream().map(i -> (ItemStack) i).toArray(ItemStack[]::new);
				}else {
					return null;
				}
			}
		}else {
			return null;
		}
		return null;
	}
	
	////////////////////////////////////////////////////////////////////////////////
	
	public boolean playerHasFile(Player p) {
		File archivo = new File(directorio.getAbsolutePath(), p.getName()+".yml");
		return archivo.exists();
	}

	public void createFilePlayer(Player p) {
		File archivo = new File(directorio.getAbsolutePath(), p.getName()+".yml");
		if(!playerHasFile(p)) {
			if(!(archivo.exists())) {
				try {
					archivo.createNewFile();
					if(archivo.exists()) {
						String toWrite = "## Please don't modify anything in this file, the plugin will do the updates automatically";
						FileOutputStream out = new FileOutputStream(archivo);
						out.write(toWrite.getBytes());
						out.flush();
						out.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public File getPlayerFile(Player p) {
		File archivo = new File(directorio.getAbsolutePath(), p.getName()+".yml");
		if(playerHasFile(p)) {
			if(archivo.exists()) {
				return archivo;
			}
		}
		return null;
	}

	public YamlConfiguration getPlayerFileConfig(Player p) {
		File archivo = this.getPlayerFile(p);
		if(playerHasFile(p)) {
			if(archivo.exists()) {
				return YamlConfiguration.loadConfiguration(archivo);
			}
		}
		return null;
	}
}
