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
		name = "removeShrine",
		pattern = "removeShrine",
		usage = "/dar removeShrine (<name>)",
		desc = "Removes the given shrine",
		permission = "dar.admin"
	)
public class ShrineRemoveCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(args.length <= 0) return false;
		
		ShrineHandler sH = DARManager.getShrineHander();
		Player player = (Player) sender;
		Shrine s = sH.getShrine(args[1], player.getWorld());
		
		if(s == null)
			Messenger.sendMessage(player, "Shrine not found");
		else {
			sH.removeShrine(sH.getShrine(args[1], player.getWorld()), player.getWorld());
			Messenger.sendMessage(player, "Shrine removed");
		}
		return true;
	}	
}
