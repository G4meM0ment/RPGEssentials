package me.G4meM0ment.Orbia.Commands.Job;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "job",
		pattern = "job",
		usage = "/job",
		desc = "Zeigt deine Zunft und was du durch sie erlernt hast",
		permission = ""
	)
public class JobCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "Du bist kein Spieler!");
			return false;
		}
		
		Player p = (Player) sender;
		String msg = "";
		
		msg += ChatColor.AQUA+"oOo---------------| Beruf |---------------oOo\n";
		if(p.hasPermission("orbia.job.miner")) {
			msg += ChatColor.AQUA+"Du hast den Beruf des Minenarbeiters:\n";
			msg += ChatColor.AQUA+"Du kannst Diamant und Eisenspitzhacken verwenden\n";
		}
		else if(p.hasPermission("orbia.job.alchemist")) {
			msg += ChatColor.AQUA+"Du hast den Beruf des Alchemisten\n";
			msg += ChatColor.AQUA+"Du kannst den Braustand verwenden und Tränke brauen\n";
		}
		else if(p.hasPermission("orbia.job.farmer")) {
			msg += ChatColor.AQUA+"Du hast den Beruf des Bauern:\n";
			msg += ChatColor.AQUA+"Du kannst Tiere vermehren, melken, Samen sähen und die Harken und Scheren verwenden\n";
		}
		else
			msg += ChatColor.RED+"Du hast keinen Beruf!";
		
		Messenger.sendMessage(p, msg);
		return true;
	}
}
