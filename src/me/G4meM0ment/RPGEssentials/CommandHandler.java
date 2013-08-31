package me.G4meM0ment.RPGEssentials;

import me.G4meM0ment.RPGItem.Handler.RPGItemCommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler{
	
	private RPGEssentials plugin;
	private PermHandler ph;
	private RPGItemCommandHandler rpgItemCmdHandler;
	
	private String mainCmd = "ge";
	private String reNatureCmd = "rn";
	private String junkieCmd = "junkie";
	private String orbiaCmd = "orbia";
	private String rpgItemCmd = "rpgitem";
	
	public CommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		ph = new PermHandler(plugin);
		rpgItemCmdHandler = new RPGItemCommandHandler(plugin);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		//main commands, handled in this class
		if(command.getName().equalsIgnoreCase(mainCmd)) {
			return exec(sender, command, label, args);
		}
		
		//RPGItem commands
		if(command.getName().equalsIgnoreCase(rpgItemCmd)) {
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		//Junkie cmds
		if(command.getName().equalsIgnoreCase(junkieCmd)) {
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		//ReNature cmds
		if(command.getName().equalsIgnoreCase(reNatureCmd)) {
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		//Orbia specific plugin cmds
		if(command.getName().equalsIgnoreCase(orbiaCmd)) {
			return rpgItemCmdHandler.exec(sender, command, label, args);
		}
		
		return false;
	}
	
	private boolean exec(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if(args.length > 0 && args[0].equals("reload") && sender instanceof Player && ph.checkReloadPerms(player)) {
			plugin.reloadRPGEssentials();
			player.sendMessage("RPGEssentials all plugins reloaded!");
		    return true;
		}
		return false;
	}
}
