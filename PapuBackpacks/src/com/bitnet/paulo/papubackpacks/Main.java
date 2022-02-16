package com.bitnet.paulo.papubackpacks;

import org.bukkit.plugin.java.JavaPlugin;

import com.bitnet.paulo.papubackpacks.database.Database;
import com.bitnet.paulo.papubackpacks.manager.BackpackManager;

public class Main extends JavaPlugin {
	
	private BackpackManager manager;
	
	private Database database;
	
	@Override
	public void onEnable() {
		this.getLogger().info("Cargando el plugin...");
		this.database = new Database(this);
		this.manager = new BackpackManager(this, database);
	}

	public BackpackManager getManager() {
		return manager;
	}
}
