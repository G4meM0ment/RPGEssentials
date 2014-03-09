package me.G4meM0ment.Orbia.Commands.Combat;

import me.G4meM0ment.Orbia.Handler.CMHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "c",
		pattern = "c",
		usage = "/c (?)",
		desc = "Aktiviert oder deaktiviert den Kampfmodus (verhindert das Fallenlassen von Gegenständen), mit '?' als Parameter wird abgefragt ob du im Kampfmodus und/oder im Kampf bist",
		permission = "herochat.join.Administratoren"
	)
public class CombatCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(!(sender instanceof Player)) {
			Messenger.sendMessage(sender, "Du bist kein Spieler!");
			return false;
		}
		
		CMHandler cmh = new CMHandler();
		Player p = (Player) sender;
		
		if(args.length > 0) {
			if(args[0].equalsIgnoreCase("?")) {
				if(plugin.getHeroes().getCharacterManager().getHero(p).isInCombat())
					Messenger.sendMessage(p, ChatColor.GRAY+"Du befindest dich im Kampf");
				else
					Messenger.sendMessage(p, ChatColor.GRAY+"Du befindest dich NICHT im Kampf");
				if(cmh.isInCombatMode(p))
					Messenger.sendMessage(p, ChatColor.GRAY+"Dein Kampfmodus ist eingeschalten");
				else
					Messenger.sendMessage(p, ChatColor.GRAY+"Dein Kampfmodus ist ausgeschalten");
				return true;
			}
		}
		cmh.toggleCombatMode(p);
		if(cmh.isInCombatMode(p))
			Messenger.sendMessage(p, ChatColor.GRAY+"Du bist jetzt im Kampfmodus!");
		else
			Messenger.sendMessage(p, ChatColor.GRAY+"Du hast den Kampfmodus verlassen!");
		return true;
	}
}
