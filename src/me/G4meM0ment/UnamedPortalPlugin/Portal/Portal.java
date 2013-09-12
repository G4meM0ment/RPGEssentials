package me.G4meM0ment.UnamedPortalPlugin.Portal;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Portal {
	
	private List<Block> blocks = new ArrayList<Block>();
	private Location destination;
	private String id;
	
	public Portal(String id, List<Block> blocks, Location destination) {
		this.id = id;
		this.blocks = blocks;
		this.destination = destination;
	}
	
	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}
		
	public List<Block> getBlocks() {
		return blocks;
	}
	public void setBlock(List<Block> blocks) {
		this.blocks = blocks;
	}
	
	public Location getDestination() {
		return destination;
	}
	public void setDestination(Location destination) {
		this.destination = destination;
	}
}
