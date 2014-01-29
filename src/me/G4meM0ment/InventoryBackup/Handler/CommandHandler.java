package me.G4meM0ment.InventoryBackup.Handler;

import java.io.File;

import me.G4meM0ment.InventoryBackup.DataStorage.InventoryData;
import me.G4meM0ment.RPGEssentials.RPGEssentials;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CommandHandler {
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private InventoryHandler invHandler;
	private InventoryData invData;
	
	public CommandHandler(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		invHandler = new InventoryHandler(plugin);
		invData = new InventoryData();
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) 
	{
		if(args.length <= 0) return false;
		Player p = getPlayer(args[0]);
		if(getPlayer(args[0]) == null)
		{
			sender.sendMessage("Player not found!");
			return true;
		}
		
		if(args.length > 1)
		{
			int fileNumber = 0;
			try
			{
				fileNumber = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage("Not an integer!");
				return true;
			}
			File[] files = invData.getPlayersBackups(p);
			if(files.length < fileNumber || fileNumber <= 0)
			{
				sender.sendMessage("Invalid file number!");
				return true;
			}
			
			invHandler.loadInventory(p, YamlConfiguration.loadConfiguration(files[fileNumber-1]));
			sender.sendMessage("Inventory of "+p.getName()+" restored to backup "+files[fileNumber].getName());
			p.sendMessage("Inventory restored from backup!");
			return true;
		}
		else 
		{
			sender.sendMessage(p.getName()+"'s Inventory Backups:");
			int i = 1;
			for(File f : invData.getPlayersBackups(p))
				sender.sendMessage(i+++": "+f.getName());
			return true;
		}
	}
	
    public Player getPlayer(final String name) 
    {
        Player[] players = Bukkit.getOnlinePlayers();
 
        Player found = null;
        String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : players) 
        {
            if (player.getName().toLowerCase().startsWith(lowerName)) 
            {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) 
                {
                    found = player;
                    delta = curDelta;
                    break;
                }
                if (curDelta == 0) break;
            }

        }
        return found;
    }
}
