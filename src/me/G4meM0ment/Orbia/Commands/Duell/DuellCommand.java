package me.G4meM0ment.Orbia.Commands.Duell;

import me.G4meM0ment.Orbia.Handler.Duell.DuellHandler;
import me.G4meM0ment.Orbia.Handler.Duell.DuellState;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "duell",
		pattern = "duell",
		usage = "/duell <name>|accept <name>",
		desc = "Sendet eine Duellanfrage oder aktzeptiert sie",
		permission = ""
	)
public class DuellCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "Du bist kein Spieler!");
			return true;
		}
		if(args.length <= 0) return false;
		
		DuellHandler dh = new DuellHandler();
		Player p = (Player) sender;
		Player r = null;
		
		if(args[0].equalsIgnoreCase("accept") && args.length >= 2) {			
			r = Bukkit.getPlayer(args[2]);
			if(r == null)
				Messenger.sendMessage(p, ChatColor.GRAY+"Spieler nicht gefunden!");
			else if(!dh.isInDuell(p, true, false) && !dh.isInDuell(r, true, false) && dh.getDuellState(p).equals(DuellState.REQUEST) && dh.getDuellState(r).equals(DuellState.REQUEST)) {
				dh.initDuell(p.getName(), true);
				Messenger.sendMessage(p, ChatColor.DARK_RED+"Duell gestartet mit "+r.getName());
				Messenger.sendMessage(r, ChatColor.DARK_RED+"Duell gestartet mit "+p.getName());
			}
		} else {
			
			r = Bukkit.getPlayer(args[1]);
			if(r == null)
				Messenger.sendMessage(p, ChatColor.GRAY+"Spieler nicht gefunden!");
			else if(!dh.isInDuell(p, true, false) && !dh.isInDuell(r, true, false)) {
				dh.initDuellRequest(p, r, true);
				Messenger.sendMessage(p, ChatColor.GRAY+"Anfrage versendet");
				Messenger.sendMessage(r, ChatColor.GRAY+"Duell Anfrage von "+p.getName()+" Tippe "+ChatColor.WHITE+"/duell accept "+p.getName()+ChatColor.GRAY+" um zu aktzeptieren");
			}
		}
		return true;
	}
}
