package me.G4meM0ment.RPGItem.Commands;

import java.util.HashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGEssentials.Commands.Command;
import me.G4meM0ment.RPGEssentials.Commands.CommandInfo;
import me.G4meM0ment.RPGEssentials.Messenger.Messenger;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.PermHandler;
import me.G4meM0ment.RPGItem.Handler.PowerHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "give",
		pattern = "give",
		usage = "/ri give <item name> (p:<name>)",
		desc = "Gives the given player (or if empty yourself) the item",
		permission = "rpgitem.admin.give"
	)
public class GiveCommand implements Command {
	@Override
	public boolean execute(RPGEssentials plugin, CommandSender sender, String... args) {   
		if(args.length <= 0) return false;
		
		ItemConfig itemConfig = new ItemConfig();
		CustomItemHandler customItemHandler = new CustomItemHandler();
		PowerHandler powerH = new PowerHandler();
		Player p = null;
		String name = getName(args);
		
		if(sender instanceof Player) {
			p = (Player) sender;
			if(!PermHandler.hasRPGItemGivePerms(p)) {
				Messenger.sendMessage(sender, "No permission");
				return true;
			}
		}
		
		for(String s : args)
			if(s.contains("p:"))
				p = Bukkit.getPlayer(s.replace("p:", ""));
		
		if(p == null) {
			Messenger.sendMessage(sender, "Can't find player");
			return true;
		}
		if(itemConfig.getFile(name) == null) {
			Messenger.sendMessage(sender, "Item not found or not loaded");
			return true;
		}
		
		int id = 0;
		if(plugin.getRPGItem().getConfig().getBoolean("useIDs"))
			id = customItemHandler.getFreeId(name);
		FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
		
		CustomItem customItem = customItemHandler.spawnCustomItem(p, new CustomItem(null, config.getString("displayName"), id, config.getInt("data"), Material.valueOf(config.getString("skin").toUpperCase()),
				config.getInt("damage"), config.getInt("damageMax"), config.getInt("durability"), config.getString("description"), config.getInt("price"), config.getString("lore"),
				Quality.valueOf(config.getString("quality").toUpperCase()), config.getString("type"), config.getString("hand"), Material.valueOf(config.getString("repair").toUpperCase()), config.getInt("durability"),
				new HashMap<String, Double>(), config.getBoolean("passive")));
		
		HashMap<String, Double> powers = new HashMap<String, Double>();
		for(String s : powerH.getItemPowers(customItem))
			powers.put(s, itemConfig.getConfig(itemConfig.getFile(customItem.getDispName())).getDouble("powers."+s));
		customItem.setPowers(powers);
		
		Messenger.sendMessage(sender, "Item given: "+name);
		return true;
	}	
	
	private String getName(String[] args) {
		String name = "";
		boolean first = true;
		for(String s : args) {
			if(s.equalsIgnoreCase("give")) continue;
			if(s.contains("p:")) continue;
			if(first)  {
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
