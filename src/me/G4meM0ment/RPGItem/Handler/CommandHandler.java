package me.G4meM0ment.RPGItem.Handler;

import java.util.HashMap;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.RPGItem;
import me.G4meM0ment.RPGItem.Converter.Converter;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.Handler.PermHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CommandHandler {

	private RPGEssentials plugin;
	private PermHandler ph;
	private CustomItemHandler customItemHandler;
	private ItemConfig itemConfig;
	private Converter converter;
	private RPGItem subplugin;
	private PowerHandler powerH;
	
	public CommandHandler(RPGEssentials plugin) 
	{
		this.plugin = plugin;
		subplugin = new RPGItem();
		ph = new PermHandler(plugin);
		customItemHandler = new CustomItemHandler(plugin);
		itemConfig = new ItemConfig();
		converter = new Converter(plugin);
		powerH = new PowerHandler();
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) 
	{
		Player player = null;
		if(sender instanceof Player)
			player = (Player) sender;
		
		if(args.length > 0 && args[0].equals("help")) 
		{
			if(sender instanceof Player)
			{
				//TODO add messenger
				return true;
			} else 
			{
				//TODO add messenger
				return true;
			}
		}
		
		if(args.length > 0 && args[0].equals("reload")) 
		{
			if(sender instanceof Player) 
			{
				if(!ph.hasRPGItemReloadPerms(player)) 
				{
					//TODO add messenger
					return true;
				}
				subplugin.reloadConfigs();
				player.sendMessage("RPGItem reloaded!");
				return true;
			} else 
			{
				subplugin.reloadConfigs();
				plugin.getLogger().info(subplugin.getLogTit()+"Configs reloaded!");
				return true;
			}
		}
		
		if(args.length > 0 && args[0].equals("list")) 
		{
			if(sender instanceof Player) 
			{
				if(!ph.hasRPGItemGivePerms(player)) 
				{
					//TODO add messenger
					return true;
				}
				player.sendMessage("Available items: "+ListHandler.getCustomItemTypes().keySet());
				return true;
			} else 
			{
				plugin.getLogger().info(subplugin.getLogTit()+"Available items: "+ListHandler.getCustomItemTypes().keySet());
				return true;
			}
		}
		
		if(args.length > 1 && args[0].equals("give") && !args[1].isEmpty()) 
		{
			Player p = null;
			String name = getName(args);
			if(sender instanceof Player) {
				p = player;
				if(!ph.hasRPGItemGivePerms(p)) 
				{
					//TODO add messenger
					return true;
				}
			}
			
			for(String s : args)
				if(s.contains("p:"))
					p = getPlayer(s.replace("p:", ""));
			
			if(p == null) 
			{
				if(sender instanceof Player)
					player.sendMessage("Cannot find player");
				else
					plugin.getLogger().info(subplugin.getLogTit()+"Cannot find player");
				//TODO add messenger
				return true;
			}
			if(itemConfig.getFile(name) == null) 
			{
				if(sender instanceof Player)
					player.sendMessage("No such item, or config wasn't loaded properly");
				else
					plugin.getLogger().info("No such item, or config wasn't loaded properly");
				//TODO add messenger
				return true;
			}
			
			int id = 0;
			if(subplugin.getConfig().getBoolean("useIDs"))
				id = customItemHandler.getFreeId(name);
			FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
			
			CustomItem customItem = customItemHandler.spawnCustomItem(p, new CustomItem(null, config.getString("displayName"), id, config.getInt("data"), Material.valueOf(config.getString("skin").toUpperCase()),
					config.getInt("damage"), config.getInt("damageMax"), config.getInt("durability"), config.getString("description"), config.getInt("price"), config.getString("lore"),
					Quality.valueOf(config.getString("quality").toUpperCase()), config.getString("type"), config.getString("hand"), Material.valueOf(config.getString("repair").toUpperCase()), config.getInt("durability"), new HashMap<String, Double>()));
			
			HashMap<String, Double> powers = new HashMap<String, Double>();
			for(String s : powerH.getItemPowers(customItem))
				powers.put(s, itemConfig.getConfig(itemConfig.getFile(customItem.getDispName())).getDouble("powers."+s));
			customItem.setPowers(powers);
			
			if(sender instanceof Player)
				player.sendMessage("Item given: "+name);
			else
				plugin.getLogger().info("Item given: "+name);
			//TODO add messenger
			return true;
		}
		
		if(args.length > 0 && args[0].equals("convert") && !args[2].isEmpty()) {
			if(sender instanceof Player) {
				if(!ph.hasRPGItemConvertPerms((Player)sender)) {
					//TODO add messenger
					return true;
				}
			}
			String argsString = getName(args);
			String oldName = argsString.split("id:")[1];
			String newName = argsString.split("id:")[2];
			CustomItem cloned = customItemHandler.getCustomItem(newName, customItemHandler.getFreeId(newName)-1);
			converter.convertCustomItems(oldName, cloned);
			return true;
		}
		return false;
	}
	
	public String getName(String[] args)
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
