package me.G4meM0ment.Orbia.Commands;

import me.G4meM0ment.Orbia.Tutorial.Stage;
import me.G4meM0ment.Orbia.Tutorial.TutorialHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "orbia",
		pattern = "orbia",
		usage = "/orbia setStage",
		desc = "Befehl für Orbia Interne",
		permission = "orbia.admin"
	)
public class OrbiaCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(args.length < 3) return false;
		
		TutorialHandler tutHandler = new TutorialHandler();
		Player p = Bukkit.getPlayer(args[1]);
		
		if(args[0].equalsIgnoreCase("setStage")) {		
			if(p != null)
				tutHandler.setStage(p, Stage.valueOf(args[2]));
			else
				Messenger.sendMessage(sender, "Player not found");
			return true;
		}
		return false;
	}
}