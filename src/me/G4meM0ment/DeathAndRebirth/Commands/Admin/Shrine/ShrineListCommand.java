package me.G4meM0ment.DeathAndRebirth.Commands.Admin.Shrine;

import me.G4meM0ment.DeathAndRebirth.DARManager;
import me.G4meM0ment.DeathAndRebirth.Framework.Shrine;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "listShrines",
		pattern = "listShrines",
		usage = "/dar listShrines",
		desc = "List all shrines of the given world",
		permission = "adrundaalgods.admin"
	)
public class ShrineListCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "You need to be a player");
			return true;
		}
		
		Player player = (Player) sender;
		ShrineHandler sH = DARManager.getShrineHander();
		String list = "Shrines: ";
		
		for(String worldName : sH.getShrineLists().keySet()) {
			list += "\n"+worldName+":";
			for(Shrine s : sH.getShrines(worldName))
				list += s.getName()+", ";
		}
		
		Messenger.sendMessage(player, list);
		return true;
	}	
}
