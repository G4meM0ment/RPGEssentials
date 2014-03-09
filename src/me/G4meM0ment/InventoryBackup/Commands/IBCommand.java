package me.G4meM0ment.InventoryBackup.Commands;

import java.io.File;

import me.G4meM0ment.InventoryBackup.DataStorage.InventoryData;
import me.G4meM0ment.InventoryBackup.Handler.InventoryHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "ib",
		pattern = "ib",
		usage = "/ib <name> (<backup id>)",
		desc = "Displays or loads the backups for the given player",
		permission = "ib.admin"
	)
public class IBCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {
		if(args.length <= 0) return false;
		Player p = Bukkit.getPlayer(args[0]);
		InventoryData invData = new InventoryData();
		InventoryHandler invHandler = new InventoryHandler(plugin);
		
		if(p == null) {
			sender.sendMessage("Player not found!");
			return true;
		}
		
		if(args.length > 1) {
			int fileNumber = 0;
			try{
				fileNumber = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				sender.sendMessage("Not an integer!");
				return true;
			}
			
			File[] files = invData.getPlayersBackups(p);
			if(files.length < fileNumber || fileNumber <= 0) {
				sender.sendMessage("Invalid file number!");
				return true;
			}
			
			invHandler.loadInventory(p, YamlConfiguration.loadConfiguration(files[fileNumber-1]));
			Messenger.sendMessage(sender, "Inventory of "+p.getName()+" restored to backup "+files[fileNumber].getName());
			Messenger.sendMessage(p, "Inventory restored from backup!");
			return true;
		} else {
			Messenger.sendMessage(sender, p.getName()+"'s Inventory Backups:");
			int i = 1;
			for(File f : invData.getPlayersBackups(p))
				sender.sendMessage(i+++": "+f.getName());
			return true;
		}
	}
}
