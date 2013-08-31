package me.G4meM0ment.RPGItem.Handler;

import me.G4meM0ment.RPGEssentials.PermHandler;
import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.DataStorage.ItemConfig;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItem;
import me.G4meM0ment.RPGItem.Handler.CustomItem.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.CustomItem.Quality;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RPGItemCommandHandler {

	private RPGEssentials plugin;
	private PermHandler ph;
	private CustomItemHandler customItemHandler;
	private ItemConfig itemConfig;
	
	public RPGItemCommandHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		ph = new PermHandler();
		customItemHandler = new CustomItemHandler(plugin);
		itemConfig = new ItemConfig();
	}
	
	public boolean exec(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if(command.getName().equalsIgnoreCase("rpgitem") && args.length > 0 && args[0].equals("give") && !args[1].isEmpty() && sender instanceof Player) {
			FileConfiguration config = itemConfig.getConfig(itemConfig.getFile(args[1]));
			customItemHandler.spawnCustomItem(player, new CustomItem(null, args[1], customItemHandler.getFreeId(args[1]), config.getInt("skinId"), config.getInt("damage"), config.getInt("durability"), 
					config.getString("description"), config.getString("price"), config.getString("lore"), Quality.valueOf(config.getString("quality").toUpperCase()),
					config.getString("type"), config.getString("hand")));
			player.sendMessage("Item given!");
			return true;
		}
		return false;
	}
}
