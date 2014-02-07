package me.G4meM0ment.RPGItem.CustomItem;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItem{
	
	private int id, dmgValue, dmgValueMax, durability, price, data, maxDurability;
	private String dispName, desc, lore, hand, type;
	private Material repair, skin;
	private Quality quality;
	private ItemStack item;
	private HashMap<String, Double> powers = new HashMap<String, Double>();
	private boolean passive;
	
	public CustomItem(ItemStack item, String dispName, int id, int data, Material skin, int dmgValue, int dmgValueMax, int durability, String desc, int price, String lore, Quality quality, String type, String hand, Material repair, int maxDurability, HashMap<String, Double> powers, boolean passive) 
	{
		this.item = item;
		this.dispName = dispName;
		this.id = id;
		this.data = data;
		this.skin = skin;
		this.dmgValue = dmgValue;
		this.dmgValueMax = dmgValueMax;
		this.durability = durability;
		this.desc = desc;
		this.price = price;
		this.lore = lore;
		this.quality = quality;
		this.type = type;
		this.hand = hand;
		this.repair = repair;
		this.maxDurability = maxDurability;
		this.powers = powers;
		this.passive = passive;
	}

	//Item getter/setter
	public ItemStack getItem() 
	{
		return item;
	}
	public void setItem(ItemStack item) 
	{
		this.item = item;
	}
	
	//Id getter/setter
	public int getId() 
	{
		return id;
	}
	public void setId(int id) 
	{
		this.id = id;
	}
	
	//Data value getter/setter
	public int getData() 
	{
		return data;
	}
	public void setData(int data) 
	{
		this.data = data;
	}
	
	//Skin getter/setter
	public Material getSkin()
	{
		return skin;
	}
	public void setSkin(Material mat)
	{
		skin = mat;
	}
	
	//Damage getter/setter
	public int getDmgValue()
	{
		return dmgValue;
	}
	public void setDmgValue(int i)
	{
		dmgValue = i;
	}
	
	//Max damage getter/setter
	public int getDmgValueMax()
	{
		return dmgValueMax;
	}
	public void setDmgValueMax(int dmgValueMax)
	{
		this.dmgValueMax = dmgValueMax;
	}
	
	//Displayname getter/setter
	public String getDispName()
	{
		return dispName;
	}
	public void setDispName(String dispName)
	{
		this.dispName = dispName;
	}

	//Description getter/setter
	public String getDesc()
	{
		return desc;
	}
	public void setDesc(String description)
	{
		this.desc = description;
	}
	
	//Price getter/setter
	public int getPrice() 
	{
		return price;
	}
	public void setPrice(int price) 
	{
		this.price = price;
	}
	
	//Lore getter/setter
	public String getLore() 
	{
		return lore;
	}
	public void setLore(String lore) 
	{
		this.lore = lore;
	}
	
	//Durability getter/setter
	public int getDurability() 
	{
		return durability;
	}
	public void setDurability(int durability)
	{
		this.durability = durability;
	}
	//Handed getter/setter
	public String getHand()
	{
		return hand;
	}
	public void setHand(String hand) 
	{
		this.hand = hand;
	}

	//Type getter/setter
	public String getType() 
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	
	//Quality getter/setter
	public Quality getQuality()
	{
		return quality;
	}
	public void setQuality(Quality quality)
	{
		this.quality = quality;
	}
	
	//repair getter/setter
	public Material getRepairMaterial()
	{
		return repair;
	}
	public void setRepairId(Material mat)
	{
		repair = mat;
	}
	
	//maxDurability getter/setter
	public int getMaxDurability()
	{
		return maxDurability;
	}
	public void setMaxDurability(int maxDurability)
	{
		this.maxDurability = maxDurability;
	}
	
	//maxDurability getter/setter
	public HashMap<String, Double> getPowers() 
	{
		return powers;
	}
	public void setPowers(HashMap<String, Double> powers) 
	{
		this.powers = powers;
	}
	
	//passive getter/setter
	public boolean isPassive() 
	{
		return passive;
	}
	public void setPassive(boolean passive) 
	{
		this.passive = passive;
	}
}
