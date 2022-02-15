package com.bitnet.paulo.papuafk.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bitnet.paulo.papuafk.Main;

public class TestCMD implements CommandExecutor {

	private final Main plugin;
	public TestCMD(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if(!(sender instanceof Player)) {
			return false;
		}else {
			Player p = (Player) sender;
			if(args.length < 1) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(this.getPlugin().isProtocolHook()) {
						this.getPlugin().getPackets().sendHologram(p, Arrays.asList("&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>", "&fEste pedazo de homosexual: &e%player_name%", "&fEsta AFK! pero ya va a regresar", "&8&m<===============[&b+&8] &c&lOJITO &8[&b+&8&m]===============>"), player);
					}
				}
			}
		}
		return true;
	}

	public Main getPlugin() {
		return plugin;
	}
}
