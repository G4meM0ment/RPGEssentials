package me.G4meM0ment.Rentables.Rentable;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Rentable {
	
	private Block sign;
	private List<Block> blocks;
	private String id, header;
	private int time, remaining = 0;
	private double price;
	private String renter = "", preRenter = "", owner;

	public Rentable(Block sign, List<Block> blocks, String id, String header, double price, int time, String owner) {
		this.sign = sign;
		this.blocks = blocks;
		this.id = id;
		this.header = header;
		this.price = price;
		this.time = time;
		this.owner = owner;
	}

	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
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

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

	public int getRemaining() {
		return remaining;
	}
	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public String getRenter() {
		return renter;
	}
	public void setRenter(String renter) {
		this.renter = renter;
	}
	
	public String getPreRenter() {
		return preRenter;
	}
	public void setPreRenter(String preRenter) {
		this.preRenter = preRenter;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
