package me.G4meM0ment.RPGItem.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.RPGItem.Handler.CustomItemHandler;
import me.G4meM0ment.RPGItem.Handler.ItemHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class BListener implements Listener{

	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private CustomItemHandler customItemHandler;
	private ItemHandler itemHandler;
	
	public BListener(RPGEssentials plugin) {
		this.plugin = plugin;
		customItemHandler = new CustomItemHandler();
		itemHandler = new ItemHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = (Player) event.getPlayer();
		ItemMeta meta = event.getPlayer().getItemInHand().getItemMeta();
		if(p == null || meta == null) return;
		
		if(itemHandler.isCustomItem(p.getItemInHand()))
			customItemHandler.itemUsed(customItemHandler.getCustomItem(p.getItemInHand()).getItem());
	}
}
