package com.bitnet.paulo.papubackpacks.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.bitnet.paulo.papubackpacks.Main;

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
