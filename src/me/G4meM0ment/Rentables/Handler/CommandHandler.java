package me.G4meM0ment.Rentables.Handler;

import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private PermHandler permHandler;
	private RentableHandler rh;
	
	public CommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		permHandler = new PermHandler(plugin);
		rh = new RentableHandler();
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		
		if(args.length > 0 && args[0].equals("help")) {
			if(sender instanceof Player) {
				//TODO add messenger
			} else {
				//TODO add messenger
			}
		}
		
		if(args.length > 0 && args[0].equals("admin") && sender instanceof Player) {
			if(permHandler.hasRentablesAdminPerm(player)) {
				if(rh.getPlayersAdminModeEnabled().contains(player)) {
					rh.getPlayersAdminModeEnabled().remove(player);
					player.sendMessage("Admin mode disabled");
				}
				else {
					rh.getPlayersAdminModeEnabled().add(player);
					player.sendMessage("Admin mode enabled");
				}
				//TODO add messenger
			}
			else {}
				//TODO add messenger
			return true;
		}
		return false;
	}

}
