package me.G4meM0ment.ReNature.Handler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.G4meM0ment.RPGEssentials.RPGEssentials.RPGEssentials;
import me.G4meM0ment.ReNature.ReNature;
import me.G4meM0ment.ReNature.CustomTypes.NBlock;

public class ReplaceHandler {
	
	private RPGEssentials plugin;
	private ReNature reNature;
	
	private static List<NBlock> blocks = new ArrayList<NBlock>();
	private int millis = 10000; //60000 normal
	private int recoverMillis = 25000; //250000 normal
	
	public ReplaceHandler(RPGEssentials plugin) {
		this.plugin = plugin;
		reNature = new ReNature();
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
				while(true) {
					if(blocks.size() > 0)
						check(0);
					plugin.getLogger().info("checked!");
					try{
						Thread.sleep(millis);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
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
			Block griefed = block.getLocation().getBlock();			
			
			if(b.getMaterial() == Material.AIR)
				griefed.breakNaturally();
			else
				griefed.setType(b.getMaterial());
			removeBlock(b);
			plugin.getLogger().info("Nature is recovering!");
		} else {
			plugin.getLogger().info("Won't spawn while you're watching!");
			return;
		}
	}
	
	public void workList() {
		for(int i = 0; i < blocks.size(); i++) {
			renew(blocks.get(i));
		}
	}
	
	private boolean checkPlayers(List<Player> players, Block b) {
		if(reNature.isDisabling())
			return false;
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
