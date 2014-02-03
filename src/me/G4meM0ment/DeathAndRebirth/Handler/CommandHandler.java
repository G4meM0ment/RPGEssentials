package me.G4meM0ment.DeathAndRebirth.Handler;

import me.G4meM0ment.DeathAndRebirth.DeathAndRebirth;
import me.G4meM0ment.DeathAndRebirth.Types.Shrine;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class CommandHandler {

	private DeathAndRebirth subplugin;
	private ShrineHandler sH;
	
	public CommandHandler()
	{
		subplugin = new DeathAndRebirth();
		sH = new ShrineHandler();
	}
	
	/**
	 * Specific command handler to process commands for Death and Rebirth
	 * @param sender
	 * @param command
	 * @param label
	 * @param args
	 * @return
	 */
	public boolean exec(CommandSender sender, Command command, String label, String[] args) 
	{
		/*
		 * Main command for dar related stuff
		 */
		if(command.getName().equalsIgnoreCase("dar") && args.length > 0)
		{
			/*
			 * utils for the config files
			 */
			if(args[0].equalsIgnoreCase("config"))
			{
				if(args.length < 2)
					sender.sendMessage("Usage: /dar config reload|save");
				
				/*
				 * reload all data from config files, data in cache is gone
				 */
				if(args[1].equalsIgnoreCase("reload"))
				{
					subplugin.reloadConfigs();
					sender.sendMessage("DeathAndRebirth configs reloaded");
				}
				
				/*
				 * save data to the config, modified content is gone
				 */
				if(args[1].equalsIgnoreCase("save"))
				{
					subplugin.saveConfigs();
					sender.sendMessage("DeathAndRebirth configs saved");
				}
				return true;
			}
			
			/*
			 * utils for the data files
			 */
			if(args[0].equalsIgnoreCase("data"))
			{
				if(args.length < 2)
					sender.sendMessage("Usage: /dar data reload|save");
				
				/*
				 * reload all data from config files, data in cache is gone
				 */
				if(args[1].equalsIgnoreCase("reload"))
				{
					subplugin.reloadDataFiles();
					sender.sendMessage("DeathAndRebirth data files reloaded");
				}
				
				/*
				 * save data to the config, modified content is gone
				 */
				if(args[1].equalsIgnoreCase("save"))
				{
					subplugin.saveDataFiles();
					sender.sendMessage("DeathAndRebirth data files saved");
				}
				return true;
			}
		}
		
		/*
		 * main cmd to edit shrines and utilitize them
		 */
		if(command.getName().equalsIgnoreCase("shrine") && args.length > 0)
		{
			//Edititing shrines is only possible ingame or in flatfiles
			if(!(sender instanceof Player))
			{
				sender.sendMessage("You aren't a player!");
				return true;
			}
			Player player = (Player) sender;
			
			/*
			 * adding a shrine to cache
			 */
			if(args[0].equalsIgnoreCase("add") && args.length >= 2)
			{
				Selection sel = subplugin.getPlugin().getWorldEdit().getSelection(player);
				
				//always requires a selected worldedit area
				if(sel == null) 
				{
					player.sendMessage("You need to select an area");
				} 
				else 
				{
					if(sH.getShrine(args[1], player.getWorld()) != null)
						player.sendMessage("Shrine already exists!");
					else if(sH.addShrine(args[1], sel.getMaximumPoint(), sel.getMinimumPoint(), player.getLocation(), true))
						player.sendMessage("Shrine created");
					else
						player.sendMessage("Couldn't add shrine, maybe world not enabled or no selection");

				}
				return true;
			}
			
			/*
			 * removing an existing shrine from cache
			 */
			if(args[0].equalsIgnoreCase("remove") && args.length >= 2)
			{
				Shrine s = sH.getShrine(args[1], player.getWorld());
				
				if(s == null)
					player.sendMessage("Shrine not found");
				else
				{
					sH.removeShrine(sH.getShrine(args[1], player.getWorld()), player.getWorld());
					player.sendMessage("Shrine removed");
				}
				
				return true;
			}
			
			/*
			 * list all existing shrines in this world
			 */
			if(args[0].equalsIgnoreCase("list"))
			{
				String list = "Shrines in world "+player.getWorld().getName()+": ";
				
				if(sH.getShrineLists().containsKey(player.getWorld().getName()))
					for(Shrine s : sH.getShrines(player.getWorld().getName()))
						list += s.getName()+", ";
				
				player.sendMessage(list);
				return true;
			}
			
			/*
			 * set the spawn of an existing shrine
			 */
			if(args[0].equalsIgnoreCase("setSpawn") && args.length >= 2)
			{
				Shrine s = sH.getShrine(args[1], player.getWorld());
				if(s == null)
				{
					player.sendMessage("Shrine not found");
					return true;
				}
				s.setSpawn(player.getLocation());
				player.sendMessage("Spawn set");
				return true;
			}
		}
		
		return false;
	}
}
