package com.bitnet.paulo.papubackpacks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.bitnet.paulo.papubackpacks.Main;

public class BackpackCommand implements CommandExecutor {
	
	private final Main plugin;
	public BackpackCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			return false;
		}else {
			Player p = (Player) sender;
			if(args.length < 1) {
				if(this.getPlugin().getManager().playerHasInventory(p)) {
					Inventory inv = this.getPlugin().getManager().getPlayerInventory(p);
					p.openInventory(inv);
				}
			}
		}
		return true;
	}

	public Main getPlugin() {
		return plugin;
	}
	
}
