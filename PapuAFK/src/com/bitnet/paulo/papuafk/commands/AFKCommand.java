package com.bitnet.paulo.papuafk.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bitnet.paulo.papuafk.Main;

public class AFKCommand implements CommandExecutor {
	
	private final Main plugin;
	public AFKCommand(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Main getPlugin() {
		return plugin;
	}
}
