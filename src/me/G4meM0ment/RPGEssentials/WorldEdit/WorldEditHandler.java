package me.G4meM0ment.RPGEssentials.WorldEdit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class WorldEditHandler {

	public WorldEditHandler() {
		
	}
	
	public List<Block> getSelectedBlocks(Selection sel) {
		List<Block> blocks = new ArrayList<Block>();
//        if (sel instanceof CuboidRegion) {
            Vector min = sel.getNativeMinimumPoint();
            Vector max = sel.getNativeMaximumPoint();
            for(int x = min.getBlockX();x <= max.getBlockX(); x=x+1){
                for(int y = min.getBlockY();y <= max.getBlockY(); y=y+1){
                    for(int z = min.getBlockZ();z <= max.getBlockZ(); z=z+1){
                        Location loc = new Location(sel.getWorld(), x, y, z);
                        blocks.add(loc.getBlock());
                    }
                }
            }
//        }
        return blocks;
	}
}
