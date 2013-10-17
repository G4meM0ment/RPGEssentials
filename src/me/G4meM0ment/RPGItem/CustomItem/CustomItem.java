package me.G4meM0ment.RPGItem.CustomItem;

import org.bukkit.inventory.ItemStack;

public class CustomItem{
	
	private int id, skinId, dmgValue, dmgValueMax, durability, price, data, repairId, maxDurability;
	private String dispName, desc, lore, hand, type;
	private Quality quality;
	private ItemStack item;
	
	public CustomItem(ItemStack item, String dispName, int id, int data, int skinId, int dmgValue, int dmgValueMax, int durability, String desc, int price, String lore, Quality quality, String type, String hand, int repairId, int maxDurability) {
		this.item = item;
		this.dispName = dispName;
		this.id = id;
		this.data = data;
		this.skinId = skinId;
		this.dmgValue = dmgValue;
		this.dmgValueMax = dmgValueMax;
		this.durability = durability;
		this.desc = desc;
		this.price = price;
		this.lore = lore;
		this.quality = quality;
		this.type = type;
		this.hand = hand;
		this.repairId = repairId;
		this.maxDurability = maxDurability;
	}

	//Item getter/setter
	public ItemStack getItem() {
		return item;
	}
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	//Id getter/setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	//Data value getter/setter
	public int getData() {
		return data;
	}
	public void setData(int data) {
		this.data = data;
	}
	
	//Skin getter/setter
	public int getSkinId() {
		return skinId;
	}
	public void setSkinId(int id) {
		skinId = id;
	}
	
	//Damage getter/setter
	public int getDmgValue() {
		return dmgValue;
	}
	public void setDmgValue(int i) {
		dmgValue = i;
	}
	
	//Max damage getter/setter
	public int getDmgValueMax() {
		return dmgValueMax;
	}
	public void setDmgValueMax(int dmgValueMax) {
		this.dmgValueMax = dmgValueMax;
	}
	
	//Displayname getter/setter
	public String getDispName() {
		return dispName;
	}
	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	//Description getter/setter
	public String getDesc() {
		return desc;
	}
	public void setDesc(String description) {
		this.desc = description;
	}
	
	//Price getter/setter
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	//Lore getter/setter
	public String getLore() {
		return lore;
	}
	public void setLore(String lore) {
		this.lore = lore;
	}
	
	//Durability getter/setter
	public int getDurability() {
		return durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	//Handed getter/setter
	public String getHand() {
		return hand;
	}
	public void setHand(String hand) {
		this.hand = hand;
	}

	//Type getter/setter
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	//Quality getter/setter
	public Quality getQuality() {
		return quality;
	}
	public void setQuality(Quality quality) {
		this.quality = quality;
	}
	
	//repair getter/setter
	public int getRepairId() {
		return repairId;
	}
	public void setRepairId(int id) {
		repairId = id;
	}
	
	//maxDurability getter/setter
	public int getMaxDurability() {
		return maxDurability;
	}
	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}
}
