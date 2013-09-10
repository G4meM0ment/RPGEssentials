package me.G4meM0ment.RPGItem.Handler;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Converter.Converter;
import me.G4meM0ment.RPGItem.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.CustomItem.Quality;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.Handler.PermHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RPGItemCommandHandler {

	private RPGEssentials plugin;
	private PermHandler ph;
	private CustomItemHandler customItemHandler;
	private ItemConfig itemConfig;
	private Converter converter;
	
	public RPGItemCommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		ph = new PermHandler(plugin);
		customItemHandler = new CustomItemHandler(plugin);
		itemConfig = new ItemConfig();
		converter = new Converter(plugin);
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		
		if(args.length > 0 && args[0].equals("help")) {
			if(sender instanceof Player) {
				//TODO add messenger
			} else {
				//TODO add messenger
			}
		}
		
		if(args.length > 0 && args[0].equals("give") && !args[1].isEmpty()) {
			Player p = null;
			String name = getName(args);
			if(sender instanceof Player) {
				p = (Player) sender;
				if(!ph.hasRPGItemGivePerms(p)) {
					//TODO add messenger
					return false;
				}
			}

			if(getPlayer(args[1]) != null) {
				p = getPlayer(args[1]);
				name = getName(rearrangeGiveArgs(args));
			}
			
			if(p == null) return false;
			FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(name));
			customItemHandler.spawnCustomItem(p, new CustomItem(null, config.getString("displayName"), customItemHandler.getFreeId(name), config.getInt("data"), config.getInt("skinId"),
					config.getInt("damage"), config.getInt("damageMax"), config.getInt("durability"), config.getString("description"), config.getInt("price"), config.getString("lore"),
					Quality.valueOf(config.getString("quality").toUpperCase()), config.getString("type"), config.getString("hand")));
			//TODO add messenger
			return true;
		}
		
		if(args.length > 0 && args[0].equals("convert") && !args[2].isEmpty()) {
			if(sender instanceof Player) {
				if(!ph.hasRPGItemConvertPerms((Player)sender)) {
					//TODO add messenger
					return false;
				}
			}
			String argsString = getName(args);
			String oldName = argsString.split("id:")[1];
			String newName = argsString.split("id:")[2];
			CustomItem cloned = customItemHandler.getCustomItem(newName, customItemHandler.getFreeId(newName)-1);
			converter.convertCustomItems(oldName, cloned);
		}
		return false;
	}
	
	public String getName(String[] args) {
		String name = "";
		boolean first = true;
		for(String s : args) {
			if(s.equalsIgnoreCase("give")) continue;
			if(first) {
				name = s;
				first = false;
			} else
				name = name+" "+s;
			if(name.split(" ").length < 2 && args.length < 2)
				name.replace(" ", "");
		}
		return name;
	}
	
    public Player getPlayer(final String name) {
        Player[] players = Bukkit.getOnlinePlayers();
 
        Player found = null;
        String lowerName = name.toLowerCase();
        int delta = Integer.MAX_VALUE;
        for (Player player : players) {
            if (player.getName().toLowerCase().startsWith(lowerName)) {
                int curDelta = player.getName().length() - lowerName.length();
                if (curDelta < delta) {
                    found = player;
                    delta = curDelta;
                    break;
                }
                if (curDelta == 0) break;
            }

        }
        return found;
    }
    
    private String[] rearrangeGiveArgs(String[] args) {
		int counter = -2;
		String[] newArgs = new String[args.length-1];
		for(String s : args) {
			if(counter <= -1) {
				counter++;
				continue;
			}
			newArgs[counter] = s;
			counter++;
		}
		return newArgs;
    }
}
