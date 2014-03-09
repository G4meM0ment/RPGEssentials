package me.G4meM0ment.Karma.Commands;

import me.G4meM0ment.Karma.Handler.PlayerHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "get",
		pattern = "get",
		usage = "/karma get <name>",
		desc = "Displays the karma of the given player",
		permission = ""
	)
public class KarmaGetCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		PlayerHandler pH = new PlayerHandler();
		
		String playerName = "";
		if(args.length > 0) {
			if(!pH.getKarmaList().containsKey(args[1])) {
				sender.sendMessage(ChatColor.GRAY+"Spieler nicht gefunden");
			} else {
				playerName = args[1];
			}
		} else if(sender instanceof Player)
			playerName = sender.getName();
		
		if(!playerName.isEmpty())
			sender.sendMessage(ChatColor.GRAY+"Karma von "+playerName+": "+ChatColor.WHITE+pH.getKarma(playerName));
		return true;
	}	
}
