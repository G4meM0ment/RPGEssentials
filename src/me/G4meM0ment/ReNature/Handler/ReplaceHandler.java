package me.G4meM0ment.ReNature.Handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.G4meM0ment.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.ReNature;
import me.G4meM0ment.ReNature.CustomTypes.NBlock;

public class ReplaceHandler {
	
	private RPGEssentials plugin;
	private ReNature reNature;
	
	private static List<NBlock> blocks = new ArrayList<NBlock>();
	private int millis = 50000;
	private int recoverMillis = 250000;
	
	public ReplaceHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		reNature = new ReNature();
	}
	public ReplaceHandler() {
	}
	
	public void addBlock(NBlock b) {
		blocks.add(b);
	}
	public void removeBlock(NBlock b) {
		blocks.remove(b);
	}
	public void removeAll() {
		blocks.removeAll(blocks);
	}
	public List<NBlock> getBlockList() {
		return blocks;
	}
	
	public void start() {
		/*recoverMillis = reNature.getConfig().getInt("recoverTime");
		millis = (recoverMillis*20)/100; */
		millis = 50000;
		
		plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
				public void run() {
					if(blocks.size() > 0)
						check(0);
				}
			}, 0, millis/5);
	}
	
	private void check(int pos) {
		NBlock block = blocks.get(pos);
		if((System.currentTimeMillis() - block.getMillis()) > recoverMillis) {
			renew(block);
			if(blocks.size()-1 >= pos+1)
				check(pos+1);
		}
	}
	
	private void renew(NBlock b) {
		if(b == null) return;
		if(!checkPlayers(b.getBlock().getWorld().getPlayers(), b.getBlock())) {

			Block block = b.getBlock();
			Block griefed;
			try {
			griefed = block.getLocation().getBlock();
			} catch(IllegalStateException e) {
				e.printStackTrace();
				griefed = block;
				
			}
			
			griefed.setTypeId(b.getMaterial());
			griefed.setData(b.getData(), false);
			
			if(griefed.getTypeId() != b.getMaterial() ||griefed.getData() != b.getData())
				addBlock(b);
			removeBlock(b);
		} else {
			return;
		}
	}
	
	private void forceRenew(NBlock b) {
		if(b == null) return;
		
		Block block = b.getBlock();			
		Block griefed = block.getLocation().getBlock();	
			
		griefed.setTypeId(b.getMaterial());
		griefed.setData(b.getData(), false);
			
		removeBlock(b);
		if(griefed.getTypeId() != b.getMaterial() || griefed.getData() != b.getData()) {
			addBlock(b);
		}
	}
	
	public void workList() {
		for(NBlock b : new ArrayList<NBlock>(getBlockList())) {
			forceRenew(b);
		}
		plugin.getLogger().info(reNature.getLogTit()+"Nature recovered completely");
	}
	
	private boolean checkPlayers(List<Player> players, Block b) {
		if(reNature.isDisabling())
			return false;
		//TODO check if online players listed...
		int dist = reNature.getConfig().getInt("playerRespawnDistance");
		for(Player p : players) {
			if(p.getLocation().distance(b.getLocation()) > dist) {
				continue;
			}
			else
				return true;
		}
		return false;
	}
	
	public boolean contains(Location l) {
		List<NBlock> blocks = getBlockList();
		for(NBlock b : blocks) {
			if(b.getBlock().getLocation() == l)
				return true;
		}
		return false;
	}
}
