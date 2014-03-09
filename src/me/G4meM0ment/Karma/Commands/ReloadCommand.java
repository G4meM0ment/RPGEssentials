package me.G4meM0ment.Karma.Commands;

import me.G4meM0ment.Karma.Karma;
import me.G4meM0ment.Karma.DataStorage.PlayerData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;

import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "reload",
		pattern = "reload",
		usage = "/karma reload",
		desc = "Reloads all files and loads data to cache",
		permission = "karma.admin"
	)
public class ReloadCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		Karma karma = plugin.getKarma();
		PlayerData pD = new PlayerData();
		
		karma.reloadConfig();
		Karma.karmaKilledGood = karma.getConfig().getInt("karmaKilledGood");
		Karma.karmaKilledBad = karma.getConfig().getInt("karmaKilledBad");
		
		pD.reloadConfig();			
		pD.loadDataFromFile();
		
		sender.sendMessage("Configs reloaded");
		return true;
	}	
}
