package me.G4meM0ment.Karma.Commands;

import me.G4meM0ment.Karma.Handler.PlayerHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "set",
		pattern = "set",
		usage = "/karma set <name> <value>",
		desc = "Sets the karma for the given player",
		permission = "karma.admin"
	)
public class KarmaSetCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		PlayerHandler pH = new PlayerHandler();
		
		if(Bukkit.getPlayer(args[1]) == null && !pH.getKarmaList().containsKey(args[1]))
			sender.sendMessage("Player not found");
		else {
			try {
				pH.setKarma(args[1], Integer.parseInt(args[2]));
			} catch(NumberFormatException e) {
				sender.sendMessage(args[2]+" is not a number!");
			}
		}
		return true;
	}	
}
