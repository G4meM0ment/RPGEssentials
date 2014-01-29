package me.G4meM0ment.RPGEssentials;

import java.util.Arrays;
import java.util.List;

import me.G4meM0ment.InventoryBackup.InventoryBackup;
import me.G4meM0ment.Orbia.Orbia;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.Rentables.Rentables;
import me.G4meM0ment.UnamedPortalPlugin.UnnamedPortalPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler{
	
	private RPGEssentials plugin;
	private PermHandler ph;
	private me.G4meM0ment.RPGItem.Handler.CommandHandler rpgItemCmdHandler;
	private me.G4meM0ment.UnamedPortalPlugin.Handler.CommandHandler uppCmdHandler;
	private me.G4meM0ment.Rentables.Handler.CommandHandler rentCmdHandler;
	private me.G4meM0ment.InventoryBackup.Handler.CommandHandler iBCmdHandler;
	private me.G4meM0ment.Orbia.Handler.CommandHandler orbiaCmdHandler;

	private RPGItem rpgitem;
	private UnnamedPortalPlugin upp;
	private Rentables rentables;
	private InventoryBackup ib;
	
	private Orbia orbia;
	
	private String mainCmd = "ge";
	private String reNatureCmd = "rn";
	private String junkieCmd = "junkie";
	private List<String> orbiaCmds = Arrays.asList("orbia", "l", "g", "c", "a", "duell", "job", "help", "hilfe");
	private String rpgItemCmd = "rpgitem";
	private String rpgItemCmdAlias = "ri";
	private String UPPCmd = "upp";
	private String rentCmd = "rentables";
	private String iBCmd = "ib";
	
	public CommandHandler(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		ph = new PermHandler(plugin);
		rpgitem = new RPGItem();
		upp = new UnnamedPortalPlugin();
		rentables = new Rentables();
		ib = new InventoryBackup();
		orbia = new Orbia();
		rpgItemCmdHandler = new me.G4meM0ment.RPGItem.Handler.CommandHandler(plugin);
		uppCmdHandler = new me.G4meM0ment.UnamedPortalPlugin.Handler.CommandHandler(plugin);
		rentCmdHandler = new me.G4meM0ment.Rentables.Handler.CommandHandler(plugin);
		iBCmdHandler = new me.G4meM0ment.InventoryBackup.Handler.CommandHandler(plugin);
		orbiaCmdHandler = new me.G4meM0ment.Orbia.Handler.CommandHandler(plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		
		//main commands, handled in this class
		if(command.getName().equalsIgnoreCase(mainCmd))
		{
			return exec(sender, command, label, args);
		}
		
		//RPGItem commands
		if(command.getName().equalsIgnoreCase(rpgItemCmd) || command.getName().equalsIgnoreCase(rpgItemCmdAlias))
		{
			if(rpgitem.isEnabled())
				return rpgItemCmdHandler.exec(sender, command, label, args);
			else {}
				//TODO add messenger
		}
		
		//Junkie cmds
		if(command.getName().equalsIgnoreCase(junkieCmd)) 
		{
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		//ReNature cmds
		if(command.getName().equalsIgnoreCase(reNatureCmd))
		{
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		//UPP cmds
		if(command.getName().equalsIgnoreCase(UPPCmd)) 
		{
			if(upp.isEnabled())
				return uppCmdHandler.exec(sender, command, label, args);
			else {}
				//TODO add messenger
		}
		
		//Rentables cmds
		if(command.getName().equalsIgnoreCase(rentCmd))
		{
			if(rentables.isEnabled())
				return rentCmdHandler.exec(sender, command, label, args);
			else {}
			//TODO add messenger
		}
		
		//IB cmds
		if(command.getName().equalsIgnoreCase(iBCmd)) 
		{
			if(ib.isEnabled())
				return iBCmdHandler.exec(sender, command, label, args);
			else {}
			//TODO add messenger
		}
		
		//Orbia specific plugin cmds
		if(orbiaCmds.contains(command.getName())) 
		{
			if(orbia.isEnabled())
				return orbiaCmdHandler.exec(sender, command, label, args);
			else {}
		}
		
		return false;
	}
	
	private boolean exec(CommandSender sender, Command command, String label, String[] args) 
	{
		if(args.length > 0 && args[0].equals("reload") && sender instanceof Player && ph.hasReloadPerms(((Player)sender)))
		{
			Player player = null;
			if(sender instanceof Player) 
			{
				player = (Player) sender;
			}
			
			if(player == null) 
			{
				plugin.reloadRPGEssentials();
				plugin.getLogger().info("All subplugins reloaded");
				return true;
			} else 
			{
				plugin.reloadRPGEssentials();
				plugin.getLogger().info("All subplugins reloaded");
				player.sendMessage("RPGEssentials all subplugins reloaded!");
				return true;
			}
		}
		return false;
	}
}
