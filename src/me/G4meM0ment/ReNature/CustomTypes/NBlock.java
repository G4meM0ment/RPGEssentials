package me.G4meM0ment.ReNature.CustomTypes;

import java.util.Collection;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class NBlock{

	private long millis;
	private Block block;
	private Material material;
	
	public NBlock(Block b) {
		this.block = block;
		millis = System.currentTimeMillis();
	}
	
	public long getMillis() {
		return millis;
	}
	public void setMillis(long millis) {
		this.millis = millis;
	}
	
	public Block getBlock() {
		return block;
	}
	public void setBlock(Block b) {
		block = b;
	}
	
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material m) {
		material = m;
	}
}
