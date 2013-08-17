package me.G4meM0ment.ReNature.Listener;

import me.G4meM0ment.RPGEssentials.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.CustomTypes.NBlock;
import me.G4meM0ment.ReNature.Handler.ReplaceHandler;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BListener implements Listener{
	
	private RPGEssentials plugin;
	private ReplaceHandler rh;
	
	public BListener(RPGEssentials plugin) {
		this.plugin = plugin;
		rh = new ReplaceHandler();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		Block b = event.getBlock();
		NBlock nb = new NBlock(b);
		rh.addBlock(nb);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		NBlock nb = new NBlock(b);
		rh.addBlock(nb);
	}
}
