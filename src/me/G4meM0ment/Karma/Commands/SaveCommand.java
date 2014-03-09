package me.G4meM0ment.Karma.Commands;

import me.G4meM0ment.Karma.Karma;
import me.G4meM0ment.Karma.DataStorage.PlayerData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;

import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "save",
		pattern = "save",
		usage = "/karma save",
		desc = "Saves cache to files",
		permission = "karma.admin"
	)
public class SaveCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		Karma karma = plugin.getKarma();
		PlayerData pD = new PlayerData();
		
		karma.saveConfig();
		pD.saveDataToFile();
		sender.sendMessage("Configs saved");
		return true;
	}	
}
