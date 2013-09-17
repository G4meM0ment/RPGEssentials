package me.G4meM0ment.RPGEssentials;

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
	private RPGItem rpgitem;
	private UnnamedPortalPlugin upp;
	private Rentables rentables;
	
	private String mainCmd = "ge";
	private String reNatureCmd = "rn";
	private String junkieCmd = "junkie";
	private String orbiaCmd = "orbia";
	private String rpgItemCmd = "rpgitem";
	private String UPPCmd = "upp";
	private String rentCmd = "rentables";
	
	public CommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		ph = new PermHandler(plugin);
		rpgitem = new RPGItem();
		upp = new UnnamedPortalPlugin();
		rentables = new Rentables();
		rpgItemCmdHandler = new me.G4meM0ment.RPGItem.Handler.CommandHandler(plugin);
		uppCmdHandler = new me.G4meM0ment.UnamedPortalPlugin.Handler.CommandHandler(plugin);
		rentCmdHandler = new me.G4meM0ment.Rentables.Handler.CommandHandler(plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//main commands, handled in this class
		if(command.getName().equalsIgnoreCase(mainCmd)) {
			return exec(sender, command, label, args);
		}
		
		//RPGItem commands
		if(command.getName().equalsIgnoreCase(rpgItemCmd)) {
			if(rpgitem.isEnabled())
				return rpgItemCmdHandler.exec(sender, command, label, args);
			else {}
				//TODO add messenger
		}
		
		//Junkie cmds
		if(command.getName().equalsIgnoreCase(junkieCmd)) {
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		//ReNature cmds
		if(command.getName().equalsIgnoreCase(reNatureCmd)) {
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		//UPP cmds
		if(command.getName().equalsIgnoreCase(UPPCmd)) {
			if(upp.isEnabled())
				return uppCmdHandler.exec(sender, command, label, args);
			else {}
				//TODO add messenger
		}
		
		//Rentables cmds
		if(command.getName().equalsIgnoreCase(rentCmd)) {
			if(rentables.isEnabled())
				return rentCmdHandler.exec(sender, command, label, args);
			else {}
			//TODO add messenger
		}
		
		//Orbia specific plugin cmds
		if(command.getName().equalsIgnoreCase(orbiaCmd)) {
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		return false;
	}
	
	private boolean exec(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length > 0 && args[0].equals("reload") && sender instanceof Player && ph.hasReloadPerms(player)) {
			plugin.reloadRPGEssentials();
			player.sendMessage("RPGEssentials all plugins reloaded!");
		    return true;
		}
		return false;
	}
}
