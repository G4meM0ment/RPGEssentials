package me.G4meM0ment.RPGItem.Handler.CustomItem;

import org.bukkit.inventory.ItemStack;

public class CustomItem{
	
	private int id, skinId, dmgValue, durability, price, defSkinId = 267, defDmgValue = 10, defDurability = 1000, defPrice = 0;
	private String dispName, desc, lore, hand, type, defDesc = "An Item", defLore = "No information are known about that item", defHand = "One-handed", defType = "sword";
	private Quality quality, defQuality = Quality.COMMON;
	private ItemStack item;
	
	public CustomItem(ItemStack item, String dispName, int id, int skinId, int dmgValue, int durability, String desc, int price, String lore, Quality quality, String type, String hand) {
		this.item = item;
		this.dispName = dispName;
		this.id = id;
		this.skinId = skinId;
		this.dmgValue = dmgValue;
		this.durability = durability;
		this.desc = desc;
		this.price = price;
		this.lore = lore;
		this.quality = quality;
		this.type = type;
		this.hand = hand;
	}
	
	public CustomItem(ItemStack item, String dispName, int id) {
		this.item = item;
		this.dispName = dispName;
		this.id = id;
		skinId = defSkinId;
		dmgValue = defDmgValue;
		desc = defDesc;
		price = defPrice;
		lore = defLore;
		quality = defQuality;
		type = defType;
		hand = defHand;
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
	
	//Skin getter/setter
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
	
	//Damage getter/setter
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
	public String getDefDesc() {
		return defDesc;
	}
	
	//Price getter/setter
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getDefPrice() {
		return defPrice;
	}
	
	//Lore getter/setter
	public String getLore() {
		return lore;
	}
	public void setLore(String lore) {
		this.lore = lore;
	}
	public String getDefLore() {
		return defLore;
	}
	
	//Durability getter/setter
	public int getDurability() {
		return durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	public int getDefDurability() {
		return defDurability;
	}

	//Handed getter/setter
	public String getHand() {
		return hand;
	}
	public void setHand(String hand) {
		this.hand = hand;
	}
	public String getDefHand() {
		return defHand;
	}

	//Type getter/setter
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefType() {
		return defType;
	}
	
	//Quality getter/setter
	public Quality getQuality() {
		return quality;
	}
	public void setQuality(Quality quality) {
		this.quality = quality;
	}
	public Quality getDefQuality() {
		return defQuality;
	}	
}
