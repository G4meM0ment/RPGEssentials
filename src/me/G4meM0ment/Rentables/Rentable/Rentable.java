package me.G4meM0ment.Rentables.Rentable;

import java.util.List;

import org.bukkit.block.Block;

public class Rentable {
	
	private Block sign;
	private List<Block> blocks;
	private String id;
	private int price, time;
	
	public Rentable(Block sign, List<Block> blocks, String id, int price, int time) {
		this.sign = sign;
		this.blocks = blocks;
		this.id = id;
		this.price = price;
		this.time = time;
	}

	public Block getSign() {
		return sign;
	}
	public void setSign(Block sign) {
		this.sign = sign;
	}

	public List<Block> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	

}
