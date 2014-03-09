package me.G4meM0ment.RPGItem.Commands;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGItem.Converter.Converter;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;

import org.bukkit.command.CommandSender;

@CommandInfo(
		name = "list",
		pattern = "list",
		usage = "/ri list",
		desc = "List all loaded items",
		permission = "rpgitem.admin.give"
	)
public class ConvertCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {   
		
		CustomItemHandler customItemHandler = new CustomItemHandler();
		Converter converter = new Converter(plugin);
		
		String argsString = getName(args);
		String oldName = argsString.split("id:")[1];
		String newName = argsString.split("id:")[2];
		
		CustomItem cloned = customItemHandler.getCustomItem(newName, customItemHandler.getFreeId(newName)-1);
		converter.convertCustomItems(oldName, cloned);
		return true;
	}
	
	private String getName(String[] args)
	{
		String name = "";
		boolean first = true;
		for(String s : args) 
		{
			if(s.equalsIgnoreCase("give")) continue;
			if(s.contains("p:")) continue;
			if(first) 
			{
				name = s;
				first = false;
			} else
				name = name+" "+s;
			
			if(name.split(" ").length < 2 && args.length < 2)
				name.replace(" ", "");
		}
		return name;
	}
}

