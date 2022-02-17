package com.bitnet.paulo.papuafk;

import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bitnet.paulo.papuafk.commands.AFKCommand;
import com.bitnet.paulo.papuafk.database.Database;
import com.bitnet.paulo.papuafk.listeners.PlayerListeners;
import com.bitnet.paulo.papuafk.manager.AFKManager;
import com.bitnet.paulo.papuafk.task.AFKTask;
import com.bitnet.paulo.papuafk.utils.HologramItem;
import com.bitnet.paulo.papuafk.utils.PacketsUtils;

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
    
    private void registraryml() {
    	configyml = new File(this.getDataFolder(), "config.yml");
        registerConfiguration();
        configuration = YamlConfiguration.loadConfiguration(configyml);
    }
	
	private PacketsUtils packets;
	private HologramItem holoItem;
	
	private boolean protocolHook = false;
	private boolean papihook = false;

	private void checkHooks() {
		if(Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
			if(Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
				this.protocolHook = true;
				this.getLogger().info("ProtocolLib a sido encontrado y enlazado a el plugin");
			}else {
				this.getLogger().warning("ProtocolLib no fue encontrado y no fue enlazado al plugin");
			}
		}else {
			this.getLogger().warning("ProtocolLib no fue encontrado y no fue enlazado al plugin");
		}
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
				this.papihook = true;
				this.getLogger().info("PlaceholderAPI a sido encontrado y enlazado a el plugin");
			}else {
				this.getLogger().warning("PlaceholderAPI no fue encontrado y no fue enlazado al plugin");
			}
		}else {
			this.getLogger().warning("PlaceholderAPI no fue encontrado y no fue enlazado al plugin");
		}
	}

	public boolean isProtocolHook() {
		return protocolHook;
	}
	
	public boolean isPapihook() {
		return papihook;
	}

	private AFKManager afkManager;
	
	private Database database;
	
	@Override
	public void onEnable() {
		this.registraryml();
		this.getLogger().info("Iniciando el plugin...");
		this.afkManager = new AFKManager(this);
		this.packets = new PacketsUtils(this);
		this.holoItem = new HologramItem(this);
		this.database = new Database(this);
		this.registerListener(new PlayerListeners(this));
		this.registerCommands();
		Bukkit.getScheduler().runTaskLater(this, this::checkHooks, 5L);
		Bukkit.getScheduler().runTaskTimer(this, new AFKTask(this), 1L, 20L);
		this.getLogger().info("Plugin correctamente iniciado");
	}

	public AFKManager getAfkManager() {
		return afkManager;
	}
	
	public PacketsUtils getPackets() {
		return packets;
	}
	
	public HologramItem getHoloItem() {
		return holoItem;
	}

	public Database getDatabase() {
		return database;
	}

	private void registerListener(Listener... listener) {
		Arrays.stream(listener).forEach(l -> {
			Bukkit.getPluginManager().registerEvents(l, this);
		});
	}
	
	private void registerCommands() {
		this.getCommand("afk").setExecutor(new AFKCommand(this));
	}
	
	public String locToString(Location loc) {
        return String.valueOf(String.valueOf(loc.getWorld().getName())) + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getYaw() + ":" + loc.getPitch();
    }
    
    public Location getLocationFromString(String string)  {
        String[] split = string.split(":");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);
        Location location = new Location(world, x, y, z, yaw, pitch);
        return location;
    }
}
