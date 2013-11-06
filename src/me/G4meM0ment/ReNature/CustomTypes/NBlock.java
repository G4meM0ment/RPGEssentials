package me.G4meM0ment.ReNature.CustomTypes;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class NBlock{

	private long millis;
	private Block block;
	private Material material;
	private Byte data;
	
	public NBlock(Block b, Material m, Byte d) {
		block = b;
		millis = System.currentTimeMillis();
		material = m;
		data = d;
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
	
	public Byte getData() {
		return data;
	}
	public void setData(Byte d) {
		data = d;
	}
}
