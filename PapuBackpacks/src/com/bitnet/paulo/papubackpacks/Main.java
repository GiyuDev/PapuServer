package com.bitnet.paulo.papubackpacks;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bitnet.paulo.papubackpacks.database.Database;
import com.bitnet.paulo.papubackpacks.listeners.PlayerListeners;
import com.bitnet.paulo.papubackpacks.manager.BackpackManager;

public class Main extends JavaPlugin {
	
	private BackpackManager manager;
	
	private Database database;
	
	@Override
	public void onEnable() {
		this.getLogger().info("Cargando el plugin...");
		this.database = new Database(this);
		this.manager = new BackpackManager(this, database);
		loadListeners(new PlayerListeners(this));
	}

	public BackpackManager getManager() {
		return manager;
	}
	
	private void loadListeners(Listener... listener) {
		Arrays.stream(listener).forEach(l -> {
			Bukkit.getPluginManager().registerEvents(l, this);
		});
	}
}
