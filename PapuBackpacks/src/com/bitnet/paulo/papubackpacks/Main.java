package com.bitnet.paulo.papubackpacks;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bitnet.paulo.papubackpacks.commands.BackpackCommand;
import com.bitnet.paulo.papubackpacks.database.Database;
import com.bitnet.paulo.papubackpacks.listeners.PlayerListeners;
import com.bitnet.paulo.papubackpacks.manager.BackpackManager;

public class Main extends JavaPlugin {
	
	public String rutaConfig;
    public static File configyml;
    public static YamlConfiguration configuration;
    
    public void registerConfiguration() {
        rutaConfig = configyml.getAbsolutePath();
        if (!(configyml.exists())) {
            setConfigurationDefaults();
        }
    }

    public void setConfigurationDefaults() {
        this.saveResource("config.yml", true);
    }
	
	private BackpackManager manager;
	
	private Database database;
	
	@Override
	public void onEnable() {
		this.getLogger().info("Cargando el plugin...");
		configyml = new File(this.getDataFolder(), "config.yml");
        registerConfiguration();
        configuration = YamlConfiguration.loadConfiguration(configyml);
		this.database = new Database(this);
		this.manager = new BackpackManager(this, database);
		loadListeners(new PlayerListeners(this));
		registerCommands();
		this.getLogger().info("Plugin correctamente iniciado");
	}

	public BackpackManager getManager() {
		return manager;
	}
	
	private void loadListeners(Listener... listener) {
		Arrays.stream(listener).forEach(l -> {
			Bukkit.getPluginManager().registerEvents(l, this);
		});
	}
	
	private void registerCommands() {
		this.getCommand("backpack").setExecutor(new BackpackCommand(this));
	}
	
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(this.getManager().playerHasInventory(p)) {
				this.getManager().saveContents(p);
			}
		}
	}
}
