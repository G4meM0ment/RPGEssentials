package me.G4meM0ment.ReNature.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.G4meM0ment.RPGEssentials.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.CustomTypes.NBlock;

public class ReplaceHandler {
	
	private RPGEssentials rpge;
	
	private static List<NBlock> blocks = new ArrayList<NBlock>();
	private int millis = 60000;
	
	public ReplaceHandler(RPGEssentials rpge) {
		this.rpge = rpge;
		//TODO get millis from config
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
	
	public void start() {
		new Thread() {
			@Override
			public void run() {
				while(Bukkit.getPluginManager().isPluginEnabled(rpge)) {
					check(0);
					try{
						Thread.sleep(millis);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	private void check(int pos) {
		NBlock block = blocks.get(pos);
		if((block.getMillis() - System.currentTimeMillis()) > 50000) {
			renew(block);
			check(pos+1);
		}
	}
	
	private void renew(NBlock b) {
		if(!checkPlayers(b.getBlock().getWorld().getPlayers(), b.getBlock())) {
			Block block = b.getBlock();
			Block griefed = block.getLocation().getBlock();
			griefed.setType(b.getMaterial());
			removeBlock(b);	
		} else {
			rpge.getLogger().info("Won't spawn while you're watching!");
			return;
		}
	}
	
	public void workList() {
		for(int i = 0; i < blocks.size()+1; i++) {
			renew(blocks.get(i));
		}
	}
	
	private boolean checkPlayers(List<Player> players, Block b) {
		//TODO check if online players listed...
		int dist = 50;
		for(Player p : players) {
			//TODO read disgtance from config
			if(p.getLocation().distance(b.getLocation()) > dist) {
				continue;
			}
			else
				return true;
		}
		return false;
	}
	

}
