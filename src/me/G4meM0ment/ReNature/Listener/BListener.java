package me.G4meM0ment.ReNature.Listener;

import java.util.List;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.ReNature;
import me.G4meM0ment.ReNature.CustomTypes.NBlock;
import me.G4meM0ment.ReNature.Handler.ReplaceHandler;
import me.G4meM0ment.ReNature.OtherPlugins.ReFaction;
import me.G4meM0ment.ReNature.OtherPlugins.ReTowny;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

public class BListener implements Listener{
	
	@SuppressWarnings("unused")
	private RPGEssentials plugin;
	private ReplaceHandler rh;
	private ReNature subplugin;
	private ReFaction reFaction;
	private ReTowny reTowny;
	
	public BListener(RPGEssentials plugin) {
		this.plugin = plugin;
		rh = new ReplaceHandler();
		subplugin = new ReNature();
		reFaction = new ReFaction(plugin);
		reTowny = new ReTowny(plugin);
	}
	
	//TODO ##### CLEANUP CODE ####### SPLIT UP LISTENERS
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		if(!subplugin.getConfig().getStringList("worlds").contains((event.getBlock().getWorld().getName())))
			return;
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		if(reFaction.isFaction(event.getBlock().getLocation()))
			return;
		if(reTowny.isTown(event.getBlock().getLocation()))
			return;
		
		if(rh.contains(event.getBlock().getLocation())) return;
		
		final BlockState bs = event.getBlockReplacedState();
		rh.addBlock(new NBlock(event.getBlock(), bs.getTypeId(), bs.getBlock().getData()));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		if(!subplugin.getConfig().getStringList("worlds").contains((event.getBlock().getWorld().getName())))
			return;
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;
		if(reFaction.isFaction(event.getBlock().getLocation()))
			return;
		if(reTowny.isTown(event.getBlock().getLocation()))
			return;

		final Block b = event.getBlock();
		
		if(subplugin.getConfig().getIntegerList("deniedIDs").contains(b.getTypeId()))
			event.setCancelled(true);
		
		NBlock nb = new NBlock(b, b.getTypeId(), b.getData());
		if(!checkRelatives(nb)) {
			event.setCancelled(true);
			return;
		}
		
		if(rh.contains(event.getBlock().getLocation())) return;
		rh.addBlock(nb);
		
		//TODO read percent from config
		int p = 1;
		int rand = (int) (Math.random()*100);
		if(rand > p) 
		{
			event.setCancelled(true);
			b.setType(Material.AIR);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onBlockSpread(BlockSpreadEvent event) {
		if(!subplugin.getConfig().getStringList("worlds").contains((event.getBlock().getWorld().getName())))
			return;
		if(reFaction.isFaction(event.getBlock().getLocation()))
			return;
		if(reTowny.isTown(event.getBlock().getLocation()))
			return;
		
		int i = 106;
		if(event.getBlock().getTypeId() != i)
			return;
		
		List<NBlock> blocks = rh.getBlockList();
		for(NBlock b : blocks) {
			if(b.getBlock().getLocation() == event.getBlock().getLocation()) {
				if(b.getMaterial() == i) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPaintingBreak(HangingBreakEvent event) {
		if(!subplugin.getConfig().getStringList("worlds").contains((event.getEntity().getWorld().getName())))
			return;
		if(reFaction.isFaction(event.getEntity().getLocation()))
			return;
		if(reTowny.isTown(event.getEntity().getLocation()))
			return;
		
		if(subplugin.getConfig().getIntegerList("deniedIDs").contains(event.getEntity().getType().getTypeId()))
			event.setCancelled(true);
	}
	
	private boolean checkRelatives(NBlock nb) {
		if(nb == null) return true;
		
		Location loc = nb.getBlock().getLocation();
		Block r = new Location(loc.getWorld(), loc.getX()+1, loc.getY(), loc.getZ()).getBlock();
		Block b = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()+1).getBlock();
		Block l = new Location(loc.getWorld(), loc.getX()-1, loc.getY(), loc.getZ()).getBlock();
		Block f = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()-1).getBlock();
		Block a = new Location(loc.getWorld(), loc.getX(), loc.getY()+1, loc.getZ()).getBlock();
		Block d = new Location(loc.getWorld(), loc.getX(), loc.getY()-1, loc.getZ()).getBlock();
		List<Integer> rIDs = subplugin.getConfig().getIntegerList("relativeIDs");
		List<Integer> dIDs = subplugin.getConfig().getIntegerList("relativeDeniedIDs");
		
		if(dIDs.contains(r.getTypeId())) {
			return false;
		}
		if(dIDs.contains(b.getTypeId())) {
			return false;
		}
		if(dIDs.contains(l.getTypeId())) {
			return false;
		}
		if(dIDs.contains(f.getTypeId())) {
			return false;
		}
		if(dIDs.contains(d.getTypeId())) {
			return false;
		}
		if(dIDs.contains(a.getTypeId())) {
			return false;
		}
		
		if(rIDs.contains(r.getTypeId())) {
			rh.addBlock(new NBlock(r, r.getTypeId(), r.getData()));
		}
		if(rIDs.contains(b.getTypeId())) {
			rh.addBlock(new NBlock(b, b.getTypeId(), b.getData()));
		}
		if(rIDs.contains(l.getTypeId())) {
			rh.addBlock(new NBlock(l, l.getTypeId(), l.getData()));
		}
		if(rIDs.contains(f.getTypeId())) {
			rh.addBlock(new NBlock(f, f.getTypeId(), f.getData()));
		}
		if(rIDs.contains(d.getTypeId())) {
			rh.addBlock(new NBlock(d, d.getTypeId(), d.getData()));
		}
		if(rIDs.contains(a.getTypeId())) {
			rh.addBlock(new NBlock(a, a.getTypeId(), a.getData()));
		}
		return true;
	}
}
