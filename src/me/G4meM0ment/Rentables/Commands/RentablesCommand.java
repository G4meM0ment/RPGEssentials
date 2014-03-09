package me.G4meM0ment.Rentables.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.Rentables.Handler.RentableHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "rentables",
		pattern = "rentables",
		usage = "/rentables admin",
		desc = "Toggles admin mode for creation of server rentables",
		permission = "rentables.admin"
	)
public class RentablesCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "You need to be a player");
			return true;
		}
		if(args.length <= 0) return false;
		if(!args[0].equalsIgnoreCase("admin")) return false;

		Player player = (Player) sender;
		RentableHandler rh = new RentableHandler();
		
		if(rh.getPlayersAdminModeEnabled().contains(player)) {
			rh.getPlayersAdminModeEnabled().remove(player);
			player.sendMessage("Admin mode disabled");
		} else {
			rh.getPlayersAdminModeEnabled().add(player);
			player.sendMessage("Admin mode enabled");
		}
		return true;
	}
}
