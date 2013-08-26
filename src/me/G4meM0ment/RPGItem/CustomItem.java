package me.G4meM0ment.RPGItem;

import org.bukkit.inventory.ItemStack;

public class CustomItem extends ItemStack{
	
	private int skinId, dmgValue, defSkinId, defDmgValue;
	private String dispName, desc, price, lore, defDesc, defPrice, defLore;
	private ItemStack item;
	
	public CustomItem(ItemStack item, String dispName, int skinId, int dmgValue, String desc, String price, String lore) {
		this.item = item;
		this.dispName = dispName;
		this.skinId = skinId;
		this.dmgValue = dmgValue;
		this.desc = desc;
		this.price = price;
		this.lore = lore;
	}
	
	public CustomItem(ItemStack item, String dispName) {
		this.item = item;
		this.dispName = dispName;
		skinId = defSkinId;
		dmgValue = defDmgValue;
		desc = defDesc;
		price = defPrice;
		lore = defLore;
	}
	
	public ItemStack getItem() {
		return item;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public int getSkinId() {
		return skinId;
	}
	public void setSkinId(int id) {
		skinId = id;
	}
	
	public int getDefSkinId() {
		return defSkinId;
	}
	public void setDefSkinId(int id) {
		defSkinId = id;
	}
	
	public int getDmgValue() {
		return dmgValue;
	}
	public void setDmgValue(int i) {
		dmgValue = i;
	}
	
	public int getDefDmgValue() {
		return defDmgValue;
	}
	public void setDefDmgValue(int i) {
		defDmgValue = i;
	}

	
	
	

}
