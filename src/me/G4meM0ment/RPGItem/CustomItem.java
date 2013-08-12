package me.G4meM0ment.RPGItem;

public class CustomItem {
	
	private int skinId, dmgValue, defSkinId, defDmgValue;
	private String dispName, desc, price, lore, defDesc, defPrice, defLore;
	
	public CustomItem(String dispName, int skinId, int dmgValue, String desc, String price, String lore) {
		this.dispName = dispName;
		this.skinId = skinId;
		this.dmgValue = dmgValue;
		this.desc = desc;
		this.price = price;
		this.lore = lore;
	}
	
	public CustomItem(String dispName) {
		this.dispName = dispName;
		skinId = defSkinId;
		dmgValue = defDmgValue;
		desc = defDesc;
		price = defPrice;
		lore = defLore;
	}
	
	

	
	
	

}
