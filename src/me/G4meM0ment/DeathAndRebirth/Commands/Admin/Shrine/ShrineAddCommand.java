package me.G4meM0ment.DeathAndRebirth.Commands.Admin.Shrine;

import me.G4meM0ment.DeathAndRebirth.DARManager;
import me.G4meM0ment.DeathAndRebirth.Handler.ShrineHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

@CommandInfo(
		name = "addShrine",
		pattern = "addShrine",
		usage = "/dar addShrine (<name>)",
		desc = "Adds a new shrine, for the given worldedit selection",
		permission = "adrundaalgods.admin"
	)
public class ShrineAddCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(args.length <= 0) return false;
		
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "You need to be a player");
			return true;
		}
		
		ShrineHandler sH = DARManager.getShrineHander();
		Player player = (Player) sender;
		
		Selection sel = plugin.getWorldEdit().getSelection(player);
			
		//always requires a selected worldedit area
		if(sel == null) {
			Messenger.sendMessage(player, "You need to select an area");
		} else {
			if(sH.getShrine(args[0], player.getWorld()) != null)
				Messenger.sendMessage(player, "Shrine already exists!");
			else if(sH.addShrine(args[0], sel.getMaximumPoint(), sel.getMinimumPoint(), player.getLocation(), true))
				Messenger.sendMessage(player, "Shrine created");
			else
				Messenger.sendMessage(player, "Couldn't add shrine, maybe world not enabled or no selection");
		}
		return true;
	}	
}