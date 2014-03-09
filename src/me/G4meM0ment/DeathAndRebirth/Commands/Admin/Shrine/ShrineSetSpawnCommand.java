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
		name = "setShrineSpawn",
		pattern = "setShrineSpawn",
		usage = "/dar setShrineSpawn (<name>)",
		desc = "Sets the spawn of the given shrine to your location",
		permission = "dar.admin"
	)
public class ShrineSetSpawnCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(args.length <= 0) return false;
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "You need to be a player");
			return true;
		}
		
		ShrineHandler sH = DARManager.getShrineHander();
		Player player = (Player) sender;
		Shrine s = sH.getShrine(args[0], player.getWorld());
		
		if(s == null)
			Messenger.sendMessage(player, "Shrine not found");
		else {
			s.setSpawn(player.getLocation());
			Messenger.sendMessage(player, "Spawn set");
		}
		return true;
	}	
}
