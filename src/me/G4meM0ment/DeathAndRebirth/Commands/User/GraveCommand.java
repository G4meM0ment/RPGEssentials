package me.G4meM0ment.DeathAndRebirth.Commands.User;

import me.G4meM0ment.DeathAndRebirth.DARManager;
import me.G4meM0ment.DeathAndRebirth.Framework.DARPlayer;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "grave",
		pattern = "grave",
		usage = "/grave",
		desc = "Shows the coordinates of your grave",
		permission = "dar.grave"
	)
public class GraveCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "You need to be a player");
			return true;
		}
		
		Player p = (Player) sender;
		DARPlayer darp = DARManager.getGhostHandler().getDARPlayer(p, p.getWorld().getName());
		String msg = "";
		
		if(darp.isDead() && darp.getGrave() != null) {
			msg = ChatColor.GRAY+"Your Grave: x: "+ChatColor.WHITE+darp.getGrave().getLocation().getBlockX()+
							  ChatColor.GRAY+"y: "+ChatColor.WHITE+darp.getGrave().getLocation().getBlockY()+
							  ChatColor.GRAY+"z: "+ChatColor.WHITE+darp.getGrave().getLocation().getBlockZ();
		} else
			msg = ChatColor.GRAY+"You're not dead or your grave is missing...";
		
		Messenger.sendMessage(p, msg);		
		return true;
	}	
}
