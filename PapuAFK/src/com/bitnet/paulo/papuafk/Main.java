package com.bitnet.paulo.papuafk;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bitnet.paulo.papuafk.commands.TestCMD;
import com.bitnet.paulo.papuafk.listeners.PlayerListeners;
import com.bitnet.paulo.papuafk.manager.AFKManager;
import com.bitnet.paulo.papuafk.task.AFKTask;
import com.bitnet.paulo.papuafk.utils.PacketsUtils;

public class Main extends JavaPlugin {
	
	private PacketsUtils packets;
	
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
	
	@Override
	public void onEnable() {
		this.getLogger().info("Iniciando el plugin...");
		this.afkManager = new AFKManager(this);
		this.packets = new PacketsUtils();
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

	private void registerListener(Listener... listener) {
		Arrays.stream(listener).forEach(l -> {
			Bukkit.getPluginManager().registerEvents(l, this);
		});
	}
	
	private void registerCommands() {
		this.getCommand("test").setExecutor(new TestCMD(this));
	}
}
